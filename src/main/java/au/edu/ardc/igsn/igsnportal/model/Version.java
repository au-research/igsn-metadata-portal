package au.edu.ardc.igsn.igsnportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

	public String id;

	public String schema;

}
