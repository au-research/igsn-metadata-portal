package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to serve the browse page.
 */
@Controller
public class BrowseController {

	Logger log = LoggerFactory.getLogger(BrowseController.class);

	IGSNRegistryService service;

	public BrowseController(IGSNRegistryService service) {
		this.service = service;
	}

	public String index(Model model, @RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size) {
		log.debug(String.format("Browsing page %s size %s", page, size));
		PaginatedIdentifiersResponse identifiersResponse = service.getPublicIdentifiers(page, size);
		log.debug("Obtained identifiersResponse totalElements:" + identifiersResponse.totalElements);
		model.addAttribute("identifiersResponse", identifiersResponse);
		model.addAttribute("identifiers", identifiersResponse.content);
		return "browse";
	}

}
