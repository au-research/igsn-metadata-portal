package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class DownloadController {

	Logger logger = LoggerFactory.getLogger(ViewController.class);

	IGSNRegistryService service;

	public DownloadController(IGSNRegistryService service) {
		this.service = service;
	}

	@GetMapping("/download")
	public ResponseEntity<ByteArrayResource> download(@RequestParam String identifierValue, @RequestParam String schema)
			throws IOException {

		String data = service.getContentForIdentifierValue(identifierValue, schema);
		String fileName = identifierValue + ".xml";
		MediaType mediaType = MediaType.APPLICATION_XML;
		if (schema.equals(IGSNRegistryService.ARDCv1JSONLD)) {
			fileName = identifierValue + ".json";
			mediaType = MediaType.APPLICATION_JSON;
		}

		ByteArrayResource resource = new ByteArrayResource(data.getBytes());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
				.contentType(mediaType).contentLength(data.getBytes().length).body(resource);
	}

}
