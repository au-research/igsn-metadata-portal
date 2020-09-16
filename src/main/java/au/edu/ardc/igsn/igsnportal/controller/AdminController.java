package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.service.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class AdminController {

	@Autowired
	SitemapService sitemapService;

	@GetMapping("/api/admin/generate-sitemap")
	public ResponseEntity<?> generateSitemap() throws MalformedURLException {
		sitemapService.generate();
		return ResponseEntity.ok().body("Done");
	}

}
