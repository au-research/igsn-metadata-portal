package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.service.SitemapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "app.sitemap.enabled")
public class AdminSitemapController {

	private static final Logger logger = LoggerFactory.getLogger(AdminSitemapController.class);

	final SitemapService sitemapService;

	public AdminSitemapController(SitemapService sitemapService) {
		this.sitemapService = sitemapService;
	}

	/**
	 * Generate or regenerate the sitemap on demand
	 * @return ResponseEntity of `done` if the request successful
	 */
	@GetMapping("/api/admin/generate-sitemap")
	@ConditionalOnProperty(name = "app.sitemap_enabled")
	public ResponseEntity<?> generateSitemap() {
		logger.info("Request to generate sitemap received");
		try {
			logger.info("Generating sitemap");
			sitemapService.generate();
		}
		catch (Exception e) {
			logger.error("Failed to generate sitemap. Reason = {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		logger.info("Sitemap Generated");
		return ResponseEntity.ok().body("Done");
	}

}
