package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.model.Identifier;
import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;

@Service
public class SitemapService {

	private static final Logger log = LoggerFactory.getLogger(SitemapService.class);

	@Autowired
	IGSNRegistryService service;

	@Value("${datadir}")
	private String dataDir;

	@Value("${baseUrl}")
	private String baseUrl;

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
		PaginatedIdentifiersResponse response = service.getPublicIdentifiers(0, 50000);
		for (Identifier identifier : response.content) {
			String urlString = String.format("%s/%s", baseUrl.replaceFirst("/$", ""), identifier.value);
			WebSitemapUrl url = new WebSitemapUrl.Options(urlString).build();
			// WebSitemapUrl url = new WebSitemapUrl.Options(urlString).lastMod(new
			// Date()).priority(1.0)
			// .changeFreq(ChangeFreq.HOURLY).build();
			wsg.addUrl(url);
		}

		wsg.write();
		wsg.writeSitemapsWithIndex();
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
