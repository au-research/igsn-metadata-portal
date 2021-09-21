package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.TestHelper;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DownloadControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IGSNRegistryService service;

	@Test
	@DisplayName("Downloading ardcv1 schema returns an inline content force download for XML")
	void download_ardcv1_returnsForceDownload() throws Exception {
		when(service.getContentForIdentifierValue("10273/XX0TUIAYLV", IGSNRegistryService.ARDCv1))
				.thenReturn(TestHelper.readFile("src/test/resources/xml/sample_ardc_v1.xml"));

		mockMvc.perform(MockMvcRequestBuilders.get("/download").param("identifierValue", "10273/XX0TUIAYLV")
				.param("schema", IGSNRegistryService.ARDCv1)).andExpect(status().isOk()).andDo(print())
				.andExpect(header().exists("Content-Disposition"))
				.andExpect(header().string("Content-Type", MediaType.APPLICATION_XML.toString()));
	}

	@Test
	@DisplayName("Downloading json-ld schema returns an inline content force download for JSON")
	void download_jsonld_returnsForceDownload() throws Exception {
		when(service.getContentForIdentifierValue("10273/XX0TUIAYLV", IGSNRegistryService.ARDCv1JSONLD))
				.thenReturn(TestHelper.readFile("src/test/resources/json/sample_ardc_v1.json"));

		mockMvc.perform(MockMvcRequestBuilders.get("/download").param("identifierValue", "10273/XX0TUIAYLV")
				.param("schema", IGSNRegistryService.ARDCv1JSONLD)).andExpect(status().isOk())
				.andExpect(header().exists("Content-Disposition"))
				.andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()));
	}

}