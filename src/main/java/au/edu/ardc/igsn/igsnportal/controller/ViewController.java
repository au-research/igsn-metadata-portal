package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.model.igsn.Resources;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class ViewController {

    IGSNRegistryService service;

    public ViewController(IGSNRegistryService service) {
        this.service = service;
    }

    @GetMapping("/view/{prefix}/{namespace}")
    public String show(
            Model model,
            @PathVariable String prefix, @PathVariable String namespace
    ) throws IOException {
        String identifierValue = String.format("%s/%s", prefix, namespace);
        String xml = service.getContentForIdentifierValue(identifierValue);
        XmlMapper xmlMapper = new XmlMapper();
        Resources resources = xmlMapper.readValue(xml, Resources.class);
        model.addAttribute("identifierValue", identifierValue);
        model.addAttribute("resource", resources.resource);
        model.addAttribute("xml", xml);
        return "view";
    }
}
