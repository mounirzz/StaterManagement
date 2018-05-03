package com.micro.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.micro.demo.models.User;
import com.micro.demo.services.MailService;
import com.micro.demo.services.UserService;

@ControllerAdvice
public class ExceptionAdapter {
	@Autowired
	UserService userService;
	
	@Autowired
	MailService mailService;
	
	@Value("${app.email.support}")
	private String supportEmail;
	
	@Value("${app.environment}")
	private String environment;
	
	public static final String DEFAULT_ERROR_VIEW = "error";
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		//if the exception is annotated with @ResponseStatuts rethrow it and let
		//the framework handle it - like the OrderNotFoundException exemple 
		//at the start of this post .
		//AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!= null) {
			throw e ;
		}
		// Otherwise setup and send the user to a default error view
		User user = userService.getLo
		return mav;
	}
}
