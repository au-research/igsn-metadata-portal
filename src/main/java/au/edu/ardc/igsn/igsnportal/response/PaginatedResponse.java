package au.edu.ardc.igsn.igsnportal.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PaginatedResponse {
    public int totalPages;
    public int size;
    public int number;
}
