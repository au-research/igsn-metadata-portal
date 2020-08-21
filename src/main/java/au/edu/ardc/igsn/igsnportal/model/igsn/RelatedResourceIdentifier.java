package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class RelatedResourceIdentifier {

    @JacksonXmlProperty(isAttribute = true)
    public String relatedResourceIdentifierType;

    @JacksonXmlText
    public String value;
}
