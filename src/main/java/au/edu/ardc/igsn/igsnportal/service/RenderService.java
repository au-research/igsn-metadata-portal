package au.edu.ardc.igsn.igsnportal.service;

import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class RenderService {

	public String renderDate(String inputDate) {
		try {
			// Parses the date
			LocalDate dt = LocalDate.parse(inputDate);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM YYYY");
			return formatter.format(dt);
		}
		catch (Exception e) {
			return null;
		}
	}

}
