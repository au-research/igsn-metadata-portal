package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedResource {

	public String relatedResourceTitle;

	@JacksonXmlElementWrapper(useWrapping = false)
	public List<RelatedResourceIdentifier> relatedResourceIdentifier;

	@JacksonXmlProperty(isAttribute = true)
	public String relationType;

	@JacksonXmlText
	public String value;

}
