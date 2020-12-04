package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.config.ApplicationProperties;
import au.edu.ardc.igsn.igsnportal.model.Identifier;
import au.edu.ardc.igsn.igsnportal.model.Record;
import au.edu.ardc.igsn.igsnportal.response.PaginatedRecordsResponse;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@Service
@ConditionalOnProperty(name = "app.sitemap.enabled")
public class SitemapService {

	private static final Logger log = LoggerFactory.getLogger(SitemapService.class);

	final IGSNRegistryService service;

	final ApplicationProperties applicationProperties;

	public SitemapService(IGSNRegistryService service, ApplicationProperties applicationProperties) {
		this.service = service;
		this.applicationProperties = applicationProperties;

		log.info("Sitemap Generation is enabled");
	}

	/**
	 * Generate the sitemap and store them in the configurated dataDir
	 * @throws MalformedURLException when the baseUrl or url generated are malformed
	 */
	@Scheduled(fixedRate = 60 * 60 * 1000)
	public void generate() throws MalformedURLException {
		String dataDir = applicationProperties.getDataPath();
		String baseUrl = applicationProperties.getPortalUrl();
		log.debug("Generating Sitemap");
		if (!check()) {
			log.error("Directory {} is not accessible, skipping sitemap generation", dataDir);
			return;
		}

		File outputDir = new File(dataDir);
		log.debug("Sitemap outputDir = {}", outputDir.toString());
		log.debug("Sitemap baseUrl = {}", baseUrl);
		WebSitemapGenerator wsg = WebSitemapGenerator.builder(baseUrl, outputDir).build();

		// todo handle more than 50000 results
		PaginatedRecordsResponse response = service.getPublicRecords(0, 50000);
		if(response == null){
			log.info("Unable to generate Sitemap from registry {}", baseUrl);
			return;
		}

		for (Record record : response.content) {

			// find the Identifier.type=IGSN
			Identifier identifier = record.identifiers.stream().filter(i -> i.type.equals(("IGSN"))).findFirst()
					.orElse(null);

			// if the record doesn't have a currentVersion or doesn't have an IGSN
			// Identifier, it's not an IGSN record, skip
			if (record.currentVersions.isEmpty() || identifier == null) {
				continue;
			}

			// build the url
			String urlString = String.format("%s/%s", baseUrl.replaceFirst("/$", "") + "/view", identifier.value);
			WebSitemapUrl url = new WebSitemapUrl.Options(urlString).lastMod(record.modifiedAt)
					// . priority(1.0)
					// .changeFreq(ChangeFreq.HOURLY)
					.build();

			wsg.addUrl(url);
		}

		List<File> sitemaps = wsg.write();
		log.info("Sitemap Generated = {}, count = {}", sitemaps, response.totalElements);
		// wsg.writeSitemapsWithIndex();
	}

	/**
	 * Check the dataDir to see if it exists and writable Attemps to create if doesn't
	 * exist
	 * @return true if the dataDir is writable
	 */
	private boolean check() {
		String dataDir = applicationProperties.getDataPath();
		log.debug("Checking directory {}", dataDir);
		File outputDir = new File(dataDir);
		if (!outputDir.exists()) {
			log.debug("Directory doesn't exist, attempting creation");
			boolean result = outputDir.mkdir();
			if (!result) {
				log.error("Failed to create directory at {} ", dataDir);
				return false;
			}
			return outputDir.canWrite();
		}

		return outputDir.canWrite();
	}

}
