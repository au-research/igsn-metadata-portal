package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contributor {
    @JacksonXmlProperty(isAttribute = true)
    public String contributorType;

    public String contributorName;

    public ContributorIdentifier contributorIdentifier;
}
