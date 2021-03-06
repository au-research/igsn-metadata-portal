package au.edu.ardc.igsn.igsnportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

	public String id;

	public String status;

	public String type;

	public String value;

	public String record;

}
