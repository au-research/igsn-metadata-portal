package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.TestHelper;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IGSNRegistryService service;

    @Test
    void show() throws Exception {
        String xml = TestHelper.readFile("src/test/resources/xml/sample_igsn_csiro_v3.xml");
        when(service.getContentForIdentifierValue(anyString())).thenReturn(xml);

        mockMvc.perform(MockMvcRequestBuilders.get("/view/10273/CSTSTDOCO1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CSTSTDOCO1")))
                .andExpect(content().string(containsString("A title worthy for kings")));

    }
}