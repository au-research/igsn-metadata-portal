package au.edu.ardc.igsn.igsnportal.config;

import au.edu.ardc.igsn.igsnportal.service.EventLoggingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	final EventLoggingService loggingService;

	public WebConfig(EventLoggingService loggingService) {
		this.loggingService = loggingService;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor(loggingService));
	}

}
