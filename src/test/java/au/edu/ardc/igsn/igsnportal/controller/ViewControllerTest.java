package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.TestHelper;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IGSNRegistryService service;

	@Test
	void show_validRecord_returnsHTML() throws Exception {
		when(service.getContentForIdentifierValue("10273/XX0TUIAYLV", IGSNRegistryService.ARDCv1))
				.thenReturn(TestHelper.readFile("src/test/resources/xml/sample_ardc_v1.xml"));

		when(service.isPublicIGSN(TestHelper.readFile("src/test/resources/xml/sample_ardc_v1.xml")))
				.thenReturn("true");

		mockMvc.perform(MockMvcRequestBuilders.get("/view/10273/XX0TUIAYLV")).andExpect(status().isOk())
				.andExpect(content().string(containsString("XX0TUIAYLV")))
				.andExpect(content().string(containsString("This Tiltle also left blank on purpose")));
	}

}