package au.edu.ardc.igsn.igsnportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

	public String id;

	public String schema;

	public boolean current;

	public Date createdAt;

}
