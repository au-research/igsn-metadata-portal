package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.model.igsn.Resource;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import au.edu.ardc.igsn.igsnportal.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

	/**
	 * Primary Controller for generating landing page for IGSN
	 * @param request the current {@link HttpServletRequest} to check if the user is
	 * logged in
	 * @param model the {@link Model} for UI rendering
	 * @param prefix IGSN is in the form of {prefix}/{namespace+value} so this route will
	 * capture it
	 * @param namespace the namespace+value field
	 * @return the thymeleaf template string that will be used
	 * @throws IOException is thrown when getContentForIdentifierValue failed to generate
	 * any plausible result
	 * @throws ServletException is thrown when failing to check for user logged in status
	 */
	@GetMapping("/view/{prefix}/{namespace}")
	public String show(HttpServletRequest request, Model model, @PathVariable String prefix,
			@PathVariable String namespace) throws IOException, ServletException {
		String identifierValue = String.format("%s/%s", prefix, namespace);
		logger.debug("Viewing identifier value: %s" + identifierValue);

		// obtain the XML to generate view page from
		String xml = service.getContentForIdentifierValue(identifierValue, IGSNRegistryService.ARDCv1);

		// obtain the JSON-LD to embed within the view page
		String jsonld = service.getContentForIdentifierValue(identifierValue, IGSNRegistryService.ARDCv1JSONLD);

		boolean canEdit = false;
		if (userService.isLoggedIn(request)) {
			canEdit = service.canEdit(identifierValue, userService.getPlainAccessToken(request));
		}

		return renderViewPage(model, identifierValue, xml, jsonld, canEdit);
	}

	/**
	 * A method to preview record without having them stored
	 * @param model the UI {@link Model}
	 * @param identifierValue request parameter identifierValue for rendering
	 * @param xml string ardc-igsn-desc-1.0 schema
	 * @return String of the thymeleaf template
	 * @throws JsonProcessingException bubbled up exception from
	 * {{@link #renderViewPage(Model, String, String, String, boolean)}}
	 */
	@GetMapping("/preview")
	public String preview(Model model, @RequestParam String identifierValue, @RequestParam String xml)
			throws JsonProcessingException {
		return renderViewPage(model, identifierValue, xml, "", true);
	}

	/**
	 * Render the IGSN View Page
	 * @param model the UI {@link Model}
	 * @param identifierValue string identifier in the form of {prefix/namespace+value}
	 * @param xml string ardc-igsn-desc-1.0 schema
	 * @param jsonld string ardc-json-ld
	 * @param canEdit boolean value to indicate if the edit button is showing
	 * @return String of the thymeleaf template
	 * @throws JsonProcessingException when failing to map xml to {@link Resources}
	 */
	private String renderViewPage(Model model, String identifierValue, String xml, String jsonld, boolean canEdit)
			throws JsonProcessingException {
		model.addAttribute("jsonld", jsonld);

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
