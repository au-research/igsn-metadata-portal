package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {
    public String registeredObjectType;
    public String resourceIdentifier;
    public String landingPage;
    public String isPublic;
    public String resourceTitle;
    public List<ResourceType> resourceTypes;
    public List<MaterialType> materialTypes;
//    public List<Classification> classifications;
}