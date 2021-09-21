package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class AlternateIdentifier {

    @JacksonXmlProperty(isAttribute = true)
    public String alternateIdentifierType;

    @JacksonXmlText
    public String value;
}
