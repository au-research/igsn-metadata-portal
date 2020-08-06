package au.edu.ardc.igsn.igsnportal.response;

import au.edu.ardc.igsn.igsnportal.model.Identifier;

import java.util.List;

public class PaginatedIdentifiersResponse extends PaginatedResponse {
    public List<Identifier> content;
}
