package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contributor {
    @JacksonXmlProperty(isAttribute = true)
    public String contributorType;

    public String contributorName;

    @JacksonXmlElementWrapper(useWrapping = false)
    public List<ContributorIdentifier> contributorIdentifier;
}
