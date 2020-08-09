package au.edu.ardc.igsn.igsnportal.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("exception", ex);
        model.addAttribute("url", request.getRequestURL());
        return "404";
    }
}
