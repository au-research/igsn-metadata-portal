package au.edu.ardc.igsn.igsnportal.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PaginatedResponse {

	public int totalPages;

	public int totalElements;

	public int size;

	public int number;

	public Pageable pageable;

	public boolean first;

	public boolean last;

}
