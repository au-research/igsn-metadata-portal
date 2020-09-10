package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogDate {

	@JacksonXmlProperty(isAttribute = true)
	public String eventType;

	@JacksonXmlText
	public String value;

}
