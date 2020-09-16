package au.edu.ardc.igsn.igsnportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IgsnPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgsnPortalApplication.class, args);
	}

}
