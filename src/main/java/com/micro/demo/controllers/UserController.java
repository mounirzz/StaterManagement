package com.micro.demo.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.micro.demo.models.User;
import com.micro.demo.repositories.UserRepository;
import com.micro.demo.services.MailService;
import com.micro.demo.services.UserService;

@Controller
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);

	@Value("${app.user.verification}")
	private String requireActivation;
	
	@Value("{app.user.root}")
	private String userRoot;
	
	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private AuthenticationManager authenticationManager ;
	
	@Autowired
	private UserService userservice ;
	
	@Autowired
	private MailService mailservice ;
	
	@RequestMapping("/user/list")
	public String list(ModelMap map) {
		Iterable<User> users = this.userRepository.findAll();
		map.addAttribute("users",users);
		return "user/list";
	}
	@RequestMapping(value="/user/register",method = RequestMethod.GET)
	public String registerPost(@Valid User user,BindingResult result) {
		if (result.hasErrors()) {
			return "user/register";
		}
		User registeredUser = userservice.register(user);
		if (registeredUser != null) {
			mailservice.sendNewRegistration(user.getEmail(), registeredUser.getToken());
			if (!requireActivation) {
				userservice.autologin(user.getUserName());
				return "redirect:/";
			}
			return "user/register-success";
		}else {
			log.error("User already exists :" + user.getUserName());
			result.rejectValue("email", "error.alreadyExists", "This username or email al ready exists, please try to reset password instead.");
			return "user/register";
		}
	}
	@RequestMapping(value = "/user/reset-password")
	public String resetPasswordEmail(User user) {
		return "user/reset-password";
	}
	@RequestMapping(value = "/user/reset-password",method = RequestMethod.POST)
	public String resetPasswordEmailPost(User user, BindingResult result) {
		User u = userRepository.findOneByEmail(user.getEmail());
		if (u == null) {
			result.rejectValue("email", "eroor.doesntExist","We could not find this email in our database");
			return "user/resert-password";
		}else {
			String resetToken = userservice.createResetPasswordToken(u, true);
			mailservice.sendResetPassword(user.getEmail(), resetToken);
		}
		return "user/reset-password-sent";
	}
	@RequestMapping(value = "/user/reset-password-change")
	public String resetPasswordChange(User user , BindingResult result,Model model) {
		User u = userRepository.findOneByToken(user.getToken());
		if (user.getToken().equals("1") || u == null) {
			result.rejectValue("activation", "error.doesntExist","We could not find this reset password request.");
		}else {
			model.addAttribute("username" , u.getUserName());
		}
		return "user/reset-password-change";
	}
	@RequestMapping(value="/user/reset-password-change", method = RequestMethod.POST)
	public ModelAndView resetPasswordChangePost(User user , BindingResult result) {
		boolean isChanged = userservice.restPassword(user);
		if (isChanged) {
			userservice.autologin(user.getUserName());
			return new ModelAndView("redirect:/");
		}else {
			return new ModelAndView("user/rest-password-change","error" , "Password could not be changed");
		}
	}
	@RequestMapping("/user/activation-send")
	public ModelAndView activationSend(User user) {
		return new ModelAndView("/user/activation-send");
	}
	@RequestMapping(value = "/user/activation-send", method = RequestMethod.POST)
	public ModelAndView activationSendPost(User user, BindingResult result) {
		User u = userservice.res
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
