package au.edu.ardc.igsn.igsnportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler({ NotFoundException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String handleNotFound(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		return "404";
	}

	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public String handleUnauthorized(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		return "401";
	}

	@ExceptionHandler({ ForbiddenException.class })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String handleForbidden(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("exception", ex);
		model.addAttribute("url", request.getRequestURL());
		return "403";
	}

}
