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
				default:
					return null;
			}
		}catch (Exception e) {
			return null;
		}
	}

	//public String getType(String curatorIdentifierType){
	//	if(curatorIdentifierType.contains("DOI"))
	//		return "DOI";
	//	if(curatorIdentifierType.contains("ORCID"))
	//		return "ORCID";
	//	return "other";
	//}

	public String getIcon(String curatorIdentifierType){
		if(curatorIdentifierType.contains("DOI")) 	return "/images/doi_icon.png";
		return null;
	}



}
