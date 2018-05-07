package com.micro.demo.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import com.micro.demo.services.UserService;

public class LoginAdapter implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	
	@Autowired
	private UserService userservice ;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		userservice.updateLastLogin(event.getAuthentication().getName());
		
	}

}
