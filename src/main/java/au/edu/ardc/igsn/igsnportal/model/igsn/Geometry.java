package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    @JacksonXmlProperty(isAttribute = true)
    public String srid;

    @JacksonXmlProperty(isAttribute = true)
    public String verticalDatum;

    @JacksonXmlProperty(isAttribute = true)
    public String geometryURI;

    @JacksonXmlText
    public String value;
}
