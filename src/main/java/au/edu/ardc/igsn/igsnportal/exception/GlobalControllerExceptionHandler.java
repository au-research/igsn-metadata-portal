package au.edu.ardc.igsn.igsnportal.exception;

import au.edu.ardc.igsn.igsnportal.config.ApplicationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	final ApplicationProperties applicationProperties;

	public GlobalControllerExceptionHandler(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@ExceptionHandler({ NotFoundException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String handleNotFound(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("editorLink",applicationProperties.getEditorUrl());
		return "404";
	}

	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public String handleUnauthorized(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("editorLink",applicationProperties.getEditorUrl());
		return "401";
	}

	@ExceptionHandler({ ForbiddenException.class })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String handleForbidden(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("editorLink",applicationProperties.getEditorUrl());
		return "403";
	}

}
