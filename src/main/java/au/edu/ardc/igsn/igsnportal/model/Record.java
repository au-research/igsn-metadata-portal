package au.edu.ardc.igsn.igsnportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {

	public String id;

	public boolean visible;

	public String title;

	public Date createdAt;

	public Date modifiedAt;

	public String ownerType;

	public String ownerID;

	public String type;

	public List<Version> currentVersions;

	public List<Identifier> identifiers;

}
