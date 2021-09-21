package au.edu.ardc.igsn.igsnportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SessionController {

	Logger log = LoggerFactory.getLogger(SessionController.class);

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String redirect) {
		log.debug("Logged In, redirecting to = {}", redirect);
		String redirectTo = redirect != null ? redirect : "/";
		return "redirect:" + redirectTo;
	}

	@GetMapping("/logout")
	public String logout(@RequestParam(required = false) String redirect, HttpServletRequest request)
			throws ServletException {
		log.debug("Logging Out");
		String redirectTo = redirect != null ? redirect : "/";
		request.logout();
		log.debug("Logged Out, redirecting to = {}", redirect);
		return "redirect:" + redirectTo;
	}

}
