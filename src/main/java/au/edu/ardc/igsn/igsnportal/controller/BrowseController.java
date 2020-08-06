package au.edu.ardc.igsn.igsnportal.controller;

import au.edu.ardc.igsn.igsnportal.response.PaginatedIdentifiersResponse;
import au.edu.ardc.igsn.igsnportal.service.IGSNRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BrowseController {

    @Autowired
    IGSNRegistryService service;

    @GetMapping(value = {"/", "browse"})
    public String index(
            Model model,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        PaginatedIdentifiersResponse identifiersResponse = service.getPublicIdentifiers(page, size);
        model.addAttribute("identifiersResponse", identifiersResponse);
        model.addAttribute("identifiers", identifiersResponse.content);
        return "browse";
    }
}
