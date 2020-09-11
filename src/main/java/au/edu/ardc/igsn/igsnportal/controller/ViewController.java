package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.model.igsn.Resource;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import au.edu.ardc.igsn.igsnportal.service.UserService;
import au.edu.ardc.igsn.igsnportal.util.Helpers;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ViewController {

	Logger logger = LoggerFactory.getLogger(ViewController.class);

	IGSNRegistryService service;

	UserService userService;

	public ViewController(IGSNRegistryService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}

	@GetMapping("/view/{prefix}/{namespace}")
	public String show(HttpServletRequest request, Model model, @PathVariable String prefix,
			@PathVariable String namespace) throws IOException, ServletException {
		String identifierValue = String.format("%s/%s", prefix, namespace);
		logger.debug("Viewing identifier value: %s" + identifierValue);
		String xml = service.getContentForIdentifierValue(identifierValue);
		logger.debug("Obtained xml from getContentForIdentifierValue");
		XmlMapper xmlMapper = new XmlMapper();
		Resources resources = xmlMapper.readValue(xml, Resources.class);

		try {
			sanitize(resources.resource);
		}
		catch (Exception ex) {
			logger.error("Failed to sanitize resource {}", ex.getMessage());
		}

		logger.debug("Mapped xml to resources");
		model.addAttribute("identifierValue", identifierValue);
		model.addAttribute("resource", resources.resource);
		model.addAttribute("xml", xml);
		boolean canEdit = false;
		if (userService.isLoggedIn(request)) {
			canEdit = service.canEdit(identifierValue, userService.getPlainAccessToken(request));
		}
		model.addAttribute("igsnURL", "http://igsn.org/" + resources.resource.resourceIdentifier);
		model.addAttribute("canEdit", canEdit);
		return "view";
	}

	/**
	 * Development usage only
	 *
	 * Enable loading of pre-existing test XML files for rendering test in-browser
	 * @param request Dependency Injected request
	 * @param model Dependency Injected UI Model
	 * @param testCase path variable that determine which test case to load
	 * @return String of the view
	 * @throws IOException when it can't find the file
	 * @throws ServletException when it can't render
	 */
	@GetMapping("/test/{case}")
	public String test(HttpServletRequest request, Model model, @PathVariable("case") String testCase)
			throws IOException, ServletException {
		String identifierValue = "10273/XX0TUIAYLV";
		String xml = "";
		boolean canEdit = true;
		if ("badWKT".equals(testCase)) {
			xml = Helpers.readFile("src/test/resources/xml/sample_ardc_v1_badWkt.xml");
		}
		else {
			xml = Helpers.readFile("src/test/resources/xml/sample_ardc_v1.xml");
		}
		XmlMapper xmlMapper = new XmlMapper();
		Resources resources = xmlMapper.readValue(xml, Resources.class);

		try {
			sanitize(resources.resource);
		}
		catch (Exception ex) {
			logger.error("Failed to sanitize resource {}", ex.getMessage());
		}

		logger.debug("Mapped xml to resources");
		model.addAttribute("identifierValue", identifierValue);
		model.addAttribute("xml", xml);
		model.addAttribute("resource", resources.resource);
		model.addAttribute("igsnURL", "http://igsn.org/" + resources.resource.resourceIdentifier);
		model.addAttribute("canEdit", canEdit);
		return "view";
	}

	/**
	 * Sanitize the ARDCv1 resource
	 *
	 * The schema allows for certain elements to be completely blank, or part of it blank.
	 * This sanitization cleans up those values to make rendering easier
	 * @param resource the ARDCv1 {@link Resource}
	 */
	private void sanitize(Resource resource) {
		if (!resource.alternateIdentifiers.isEmpty()) {
			resource.alternateIdentifiers.removeIf(String::isEmpty);
		}
		if (!resource.classifications.isEmpty()) {
			resource.classifications.removeIf(item -> isEmpty(item.classificationURI) && isEmpty(item.value));
		}
		if (!resource.sampledFeatures.isEmpty()) {
			resource.sampledFeatures.removeIf(item -> isEmpty(item.sampledFeatureURI) && isEmpty(item.value));
		}
	}

	/**
	 * Helper method to check if a string is empty or not Empty means it's either null or
	 * is an empty string
	 * @param str the string in question
	 * @return true if the string is empty
	 */
	private boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

}
