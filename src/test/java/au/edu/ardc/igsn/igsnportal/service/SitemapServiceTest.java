package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.model.Identifier;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SitemapService.class })
@TestPropertySource(locations = "/application.properties")
class SitemapServiceTest {

	@Autowired
	SitemapService sitemapService;

	@MockBean
	IGSNRegistryService igsnRegistryService;

	@Value("${datadir}")
	String dataDir;

	@Test
	void testGenerateSitemap() throws IOException {
		int count = 2000;

		// given ${count} results from the Registry
		PaginatedIdentifiersResponse response = new PaginatedIdentifiersResponse();
		response.content = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Identifier identifier = new Identifier();
			identifier.value = "10273/" + UUID.randomUUID().toString();
			response.content.add(identifier);
		}
		response.size = count;
		when(igsnRegistryService.getPublicIdentifiers(anyInt(), anyInt())).thenReturn(response);

		// when creating sitemap
		sitemapService.generate();

		// expects sitemap.xml is created
		File sitemapFile = new File(dataDir + "sitemap.xml");
		assertThat(sitemapFile).exists();

		// sitemap.xml contains ${count}
		String sitemapContent = new String(Files.readAllBytes(sitemapFile.toPath()));
		assertThat(StringUtils.countOccurrencesOf(sitemapContent, "<loc>")).isEqualTo(count);
	}

	@BeforeEach
	void setUp() throws IOException {
		FileUtils.deleteDirectory(new File(dataDir));
		FileUtils.forceMkdir(new File(dataDir));
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.deleteDirectory(new File(dataDir));
	}

}