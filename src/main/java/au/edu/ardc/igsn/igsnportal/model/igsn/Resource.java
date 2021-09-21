package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

	public String registeredObjectType;

	public String resourceIdentifier;

	public String landingPage;

	public String isPublic;

	public String resourceTitle;

	public List<AlternateIdentifier> alternateIdentifiers;

	public List<ResourceType> resourceTypes;

	public List<MaterialType> materialTypes;

	public List<Classification> classifications;

	public String purpose;

	public List<SampledFeature> sampledFeatures;

	public Location location;

	public Date date;

	public Method method;

	public String campaign;

	public String comments;

	public LogDate logDate;

	public List<Curation> curationDetails;

	public List<Contributor> contributors;

	public List<RelatedResource> relatedResources;

}