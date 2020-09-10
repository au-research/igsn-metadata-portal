package au.edu.ardc.igsn.igsnportal.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pageable {

	public int pageNumber;

	public int pageSize;

	public int offset;

}
