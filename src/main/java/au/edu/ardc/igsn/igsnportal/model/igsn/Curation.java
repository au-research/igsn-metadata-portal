package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Curation {
    public Curator curator;
    public String curationDate;
    public String curationLocation;
    public CuratingInstitution curatingInstitution;
}
