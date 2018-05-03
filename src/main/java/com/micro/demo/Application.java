package com.micro.demo;

import java.util.Date;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class Application {

	public static Logger log = Logger.getLogger(Application.class.getName());
	public static void main (String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("message", "Hello World");
		model.put("title", "Hello Home");
		model.put("date", new Date());
		return "index";
	}
}
