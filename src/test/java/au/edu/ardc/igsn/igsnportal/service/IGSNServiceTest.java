package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.model.igsn.Resource;
import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IGSNServiceTest {

    @Autowired
    IGSNRegistryService service;

    @Test
    void name() throws JsonProcessingException {
        String xml = service.getContentForIdentifierValue("20.500.11812/XXAB000L4");
        XmlMapper xmlMapper = new XmlMapper();
        Resources resources = xmlMapper.readValue(xml, Resources.class);
        Resource resource = resources.resource;
        System.out.println(resources);
    }
}
