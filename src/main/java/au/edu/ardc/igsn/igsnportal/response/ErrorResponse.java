package au.edu.ardc.igsn.igsnportal.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    public String message;
    public Date timestamp;
}
