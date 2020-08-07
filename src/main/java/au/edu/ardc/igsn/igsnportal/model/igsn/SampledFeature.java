package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SampledFeature {

    @JacksonXmlProperty(isAttribute = true)
    public String sampledFeatureURI;

    @JacksonXmlText
    public String value;
}
