package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.config.ApplicationProperties;
import au.edu.ardc.igsn.igsnportal.model.Identifier;
import au.edu.ardc.igsn.igsnportal.model.Record;
import au.edu.ardc.igsn.igsnportal.model.Version;
import au.edu.ardc.igsn.igsnportal.response.PaginatedRecordsResponse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SitemapService.class, ApplicationProperties.class })
@TestPropertySource(locations = "/application.properties")
class SitemapServiceTest {

	@Autowired
	SitemapService sitemapService;

	@MockBean
	IGSNRegistryService igsnRegistryService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Test
	@DisplayName("When creating sitemap, sitemap.xml file is created and contains all provided links")
	void testGenerateSitemap() throws IOException {
		int count = 2000;

		// given ${count} results from the Registry
		PaginatedRecordsResponse response = new PaginatedRecordsResponse();
		response.content = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Version version = new Version();

			Identifier identifier = new Identifier();
			identifier.type = "IGSN";
			identifier.value = "10273/" + UUID.randomUUID().toString();

			Record record = new Record();
			record.modifiedAt = new Date();
			record.currentVersions = new ArrayList<>();
			record.currentVersions.add(version);
			record.identifiers = new ArrayList<>();
			record.identifiers.add(identifier);

			response.content.add(record);
		}
		response.size = count;
		when(igsnRegistryService.getPublicRecords(anyInt(), anyInt())).thenReturn(response);

		// when creating sitemap
		sitemapService.generate();

		// expects sitemap.xml is created
		File sitemapFile = new File(applicationProperties.getDataPath() + "sitemap.xml");
		assertThat(sitemapFile).exists();

		// sitemap.xml contains ${count}
		String sitemapContent = new String(Files.readAllBytes(sitemapFile.toPath()));
		assertThat(StringUtils.countOccurrencesOf(sitemapContent, "<loc>")).isEqualTo(count);
	}

	@BeforeEach
	void setUp() throws IOException {
		FileUtils.deleteDirectory(new File(applicationProperties.getDataPath()));
		FileUtils.forceMkdir(new File(applicationProperties.getDataPath()));
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.deleteDirectory(new File(applicationProperties.getDataPath()));
	}

}