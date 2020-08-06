package au.edu.ardc.igsn.igsnportal.response;

import au.edu.ardc.igsn.igsnportal.model.Version;

import java.util.List;

public class PaginatedVersionResponse extends PaginatedResponse {
    public List<Version> content;
}
