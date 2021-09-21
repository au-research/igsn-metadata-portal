package au.edu.ardc.igsn.igsnportal.config;

import au.edu.ardc.igsn.igsnportal.service.EventLoggingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	final EventLoggingService loggingService;

	final ApplicationProperties applicationProperties;

	public WebConfig(EventLoggingService loggingService, ApplicationProperties applicationProperties) {
		this.loggingService = loggingService;
		this.applicationProperties = applicationProperties;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor(loggingService));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/sitemap**").addResourceLocations("file://" + applicationProperties.getDataPath());
		registry.addResourceHandler("/robots.txt")
				.addResourceLocations("file://" + applicationProperties.getDataPath());
	}

}
