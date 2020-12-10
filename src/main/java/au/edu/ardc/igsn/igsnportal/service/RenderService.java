package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.config.ApplicationProperties;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class RenderService {

	public String renderDate(String inputDate){

		String [] dateParts = inputDate.split("-");
		Integer formats = dateParts.length;
		switch (formats) {
			case 1:
				//no need to parse we have received either YYYY or unknown string input
				return inputDate;
			case 2:
				try {
					// Parses the date we have received YYYY-MM
					YearMonth thisYearMonth = YearMonth.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]));
					// Create a DateTimeFormatter string
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
					return thisYearMonth.format(formatter);
				}
				catch(Exception ex){
					return inputDate;
				}
			default:
				try {
					// Parses the full date
					if(inputDate.length()>10) inputDate = inputDate.substring(0,10);
					LocalDate dt = LocalDate.parse(inputDate);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM YYYY");
					return formatter.format(dt);
				}
				catch(Exception ex){
					return inputDate;
				}
		}
	}

	public String renderIdentifier(String identifier, String identifierType) {
		try{
			switch (identifierType){
				case "DOI":
					if(!identifier.contains("http")) { identifier = "https://doi.org/" + identifier;}
					return identifier;
				case "ORCID":
					if(!identifier.contains("http")) { identifier = "http://orcid.org/" + identifier;}
					return identifier;
				case "ARK":
					if(identifier.contains("http")) { return identifier; }
					return null;
				case "Handle":
					if(!identifier.contains("http")) { identifier = "http://hdl.handle.net/" + identifier; }
					return identifier;
				case "IGSN":
					if(!identifier.contains("http://") && !identifier.contains("https://") && !identifier.contains("/"))
						{ identifier = "http://igsn.org/" + identifier; }
					if(!identifier.contains("http://") && !identifier.contains("https://") && identifier.contains("/"))
					{ identifier = "http://hnd.handle.net/" + identifier; }
					return identifier;
				case "ISNI":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "http://www.isni.org/" + identifier;}
					return identifier;
				case "ISSN":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://urn.issn.org/urn:issn:" + identifier;}
					return identifier;
				case "LISSN":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://urn.issn.org/urn:issn:" + identifier + "?+issnl" ;}
					return identifier;
				case "ISSN-L":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://urn.issn.org/urn:issn:" + identifier + "?+issnl" ;}
					return identifier;
				case "EISSN":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://urn.issn.org/urn:issn:" + identifier;}
					return identifier;
				case "PURL":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "http://purl.org/" + identifier;}
					return identifier;
				case "RAID":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "http://hdl.handle.net/" + identifier; }
					return identifier;
				case "VIAF":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "http://viaf.org/viaf/" + identifier; }
					return identifier;
				case "arXiv":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://arxiv.org/abs/" + identifier; }
					return identifier;
				case "NLA":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "http://nla.gov.au/" + identifier; }
					return identifier;
				case "PMID":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://pubmed.ncbi.nlm.nih.gov/" + identifier; }
					return identifier;
				case "ROR":
					if(!identifier.contains("http://") && !identifier.contains("https://"))
					{ identifier = "https://ror.org/" + identifier; }
					return identifier;
				default:
					if(identifier.contains("http://") || identifier.contains("https://") ) { return identifier; }
					return null;
			}
		}catch (Exception e) {
			return null;
		}
	}

	public String getIcon(String identifierType){
		try{
			switch (identifierType){
				case "DOI":
					return "/images/doi_icon.png";
				case "ORCID":
					return "/images/orcid_icon.png";
				case "NLA":
					return "/images/nla_icon.png";
				case "Handle":
					return "/images/handle_icon.png";
				default:
					return "/images/external_link.png";
			}
		}catch (Exception e) {
			return null;
		}
	}



}
