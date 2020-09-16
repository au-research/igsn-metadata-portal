package au.edu.ardc.igsn.igsnportal.config;

import au.edu.ardc.igsn.igsnportal.service.EventLoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	final EventLoggingService loggingService;

	@Value("${datadir}")
	private String dataDir;

	public WebConfig(EventLoggingService loggingService) {
		this.loggingService = loggingService;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor(loggingService));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/sitemap**").addResourceLocations("file://" + dataDir);
		registry.addResourceHandler("/robots.txt").addResourceLocations("file://" + dataDir);
	}

}
