package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.model.Identifier;
import au.edu.ardc.igsn.igsnportal.model.Record;
import au.edu.ardc.igsn.igsnportal.response.PaginatedRecordsResponse;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class SitemapService {

	private static final Logger log = LoggerFactory.getLogger(SitemapService.class);

	final IGSNRegistryService service;

	@Value("${datadir}")
	private String dataDir;

	@Value("${baseUrl}")
	private String baseUrl;

	public SitemapService(IGSNRegistryService service) {
		this.service = service;
	}

	public void generate() throws MalformedURLException {
		log.debug("Generating Sitemap");
		if (!check()) {
			log.error("Failed checking directory, skipping sitemap generation");
			return;
		}

		File outputDir = new File(dataDir);
		log.debug("Sitemap outputDir = {}", outputDir.toString());
		log.debug("Sitemap baseUrl = {}", baseUrl);
		WebSitemapGenerator wsg = WebSitemapGenerator.builder(baseUrl, outputDir).build();

		// todo handle more than 50000 results
		PaginatedRecordsResponse response = service.getPublicRecords(0, 50000);
		for (Record record : response.content) {
			Identifier identifier = record.identifiers.stream().filter(i -> i.type.equals(("IGSN"))).findFirst()
					.orElse(null);
			if (record.currentVersions.isEmpty() || identifier == null) {
				continue;
			}

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

	private boolean check() {
		log.debug("Checking directory {}", dataDir);
		File outputDir = new File(dataDir);
		if (!outputDir.exists()) {
			log.debug("Directory doesn't exist, attempting creation");
			outputDir.mkdir();
			return outputDir.canWrite();
		}

		return outputDir.canWrite();
	}

}
