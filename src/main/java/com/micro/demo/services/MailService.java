package com.micro.demo.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.micro.demo.models.User;

@Service
public class MailService {
	
	@Value("${app.email.from}")
	private String fromEmail;
	
	@Value("${app.url}")
	private String appUrl;
	
	@Value("${app.email.support}")
	private String supportEmail;
	
	@Autowired
	private MailSender mailSender;
	
	public void sendEmail(String to , String subject , String text) {
		try {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(to);
			email.setSubject(subject);
			email.setFrom(fromEmail);
			email.setText(text);
			mailSender.send(email);
			System.out.println("SENT EMAIL : TO=" + to + "|SUBJECT:" + subject + "|TEXT:"+ text);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	public void sendResetPassword(String to , String token) {
		String url = appUrl + "/user/rest-password-change?token=" +token;
		String subject = "Reset Password";
		String text = "Please click the following link to reset your password: " + url;
		sendEmail(to, subject , text);
	}
	public void sendNewRegistration(String to , String token) {
		 String url = appUrl + "/user/activate?activation=" + token;
	        String subject = "Please activate your account";
	        String text = "Please click the following link to activate your account: " + url;
	        sendEmail(to, subject, text);
	}
	public void sendNewActivationRequest(String to , String token) {
		sendNewRegistration(to, token);
	}
	public void senErrorEmail(Exception e, HttpServletRequest req,User user) {
		String subject = "Application Error:" +req.getRequestURI();
		String text = "An Error occured in your application: " + e + "\r\nFor user: " + user.getEmail();
		sendEmail(supportEmail,subject, text);
	}
	
}
