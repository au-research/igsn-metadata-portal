package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.service.SitemapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Admin related tasks The method(s) here should be protected by the admin
 * role configured in the Security Configuration
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);

	final SitemapService sitemapService;

	public AdminController(SitemapService sitemapService) {
		this.sitemapService = sitemapService;
	}

	/**
	 * Generate or regenerate the sitemap on demand
	 * @return ResponseEntity of `done` if the request successful
	 */
	@GetMapping("generate-sitemap")
	public ResponseEntity<?> generateSitemap() {
		log.info("Request to generate sitemap received");
		try {
			log.info("Generating sitemap");
			sitemapService.generate();
		}
		catch (Exception e) {
			log.error("Failed to generate sitemap. Reason = {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		log.info("Sitemap Generated");
		return ResponseEntity.ok().body("Done");
	}

}
