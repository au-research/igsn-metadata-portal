package au.edu.ardc.igsn.igsnportal.model.igsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Curator {
    public String curatorName;

    @JacksonXmlElementWrapper(useWrapping = false)
    public List<CuratorIdentifier> curatorIdentifier;
}
