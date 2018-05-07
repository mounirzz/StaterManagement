package com.micro.demo.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.mail.Multipart;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import com.micro.demo.models.User;
import com.micro.demo.repositories.UserRepository;
import com.micro.demo.services.MailService;
import com.micro.demo.services.UserService;

@Controller
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);

	@Value("${Spring-stater.user.verification}")
	private boolean requireActivation;
	
	@Value("{Spring-stater.user.root}")
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
		User u = userservice.resetActivation(user.getEmail());
		if (u != null) {
			mailservice.sendNewActivationRequest(u.getEmail(), u.getToken());
			return new ModelAndView("/user/activation-sent");
		}else {
			result.rejectValue("email", "error.doesntExist","We could not find this email in our database");
			return new ModelAndView("/user/activation-send");
		}
	}
	@RequestMapping("/user/delete")
	public String delete(Long id) {
		userservice.delete(id);
		return "redirect:/user/list";
	}
	@RequestMapping("/user/activate")
	public String activate(String activation) {
		User u = userservice.activate(activation);
		if (u != null) {
			userservice.autologin(u);
			return "redirect:/";
		}
		return "redirect:/error?message=Could not activate with this activation code, please contact support";
	}
	@RequestMapping("/user/autologin")
	public String autologin(User user) {
		userservice.autologin(user.getUserName());
		return "redirect:/";
	}
	@RequestMapping("/user/edit/{id}")
	public String edit(@PathVariable("id") Long id , User user) {
		User u ;
		User loggedInUser = userservice.getLoggedInUser();
		if (id == 0) {
			id = loggedInUser.getId();
		}
		if (loggedInUser.getId() != id && !loggedInUser.isAdmin()) {
			return "user/permission-denied";
		}else if(loggedInUser.isAdmin()) {
			u = userRepository.findOne(id);
		} else {
			u = loggedInUser;
		}
		user.setId(u.getId());
		user.setUserName(u.getUserName());
		user.setAddress(u.getAddress());
		user.setCompanyName(u.getCompanyName());
		user.setEmail(u.getEmail());
		user.setFirstname(u.getFirstname());
		user.setLastname(u.getLastname());
		
		return "/user/edit";
	}
	@RequestMapping(value= "/user/edit", method = RequestMethod.POST)
	public String editPost(@Valid User user,BindingResult result) {
		if (result.hasFieldErrors("email")) {
			return "/user/edit";
		}
		if (userservice.getLoggedInUser().isAdmin()) {
			userservice.updateUser(user);
		}else {
			userservice.updateUser(userservice.getLoggedInUser().getUserName(),user);
		}
		if (userservice.getLoggedInUser().getId().equals(user.getId())) {
			userservice.getLoggedInUser(true);
		}
		return "redirect:/user/edit/" +user.getId() + "?updated";
	}
	@RequestMapping(value="/user/update", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		String fileName = formatter.format(Calendar.getInstance().getTime())+ "_thumbnail.jpg";
		User user = userservice.getLoggedInUser();
		if(!file.isEmpty()){
			try {
				String saveDirectory = userRoot + File.separator + user.getId() + File.separator;
				File test = new File(saveDirectory);
				if (!test.exists()) {
					test.mkdirs();
				}
				byte[] bytes = file.getBytes();
				
				ByteArrayInputStream imageInputStream = new ByteArrayInputStream(bytes);
				BufferedImage image = ImageIO.read(imageInputStream);
				// BufferedImage thumbnail = scalr.resize(image, 200);
				
				File thumbnailOut = new File(saveDirectory + fileName);
				ImageIO.write(null, "png", thumbnailOut);
				
				userservice.updateProfilePicture(user, fileName);
				userservice.getLoggedInUser(true); //Force refresh of cached User
				System.out.println("image Saved::: "+ fileName);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "redirect:/user/edit/" +user.getId();
	
	}
	@RequestMapping(value="/user/profile-picture", method = RequestMethod.GET)
	public @ResponseBody byte[] profilePicture() throws IOException {
		User u = userservice.getLoggedInUser();
		String profilePicture = userRoot = File.separator + u.getId() + File.separator + u.getProfilePicture();
		if (new File(profilePicture).exists()) {
			return IOUtils.toByteArray(new FileInputStream(profilePicture));
		}else {
			return null;
		}
	}
	
	
	
	
}
