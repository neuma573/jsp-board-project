package com.lguplus.medialog.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {

	@RequestMapping("/")
    public String home() {
		return "redirect:/view/home";
    }

	@GetMapping("/login")
	public String login() {
		return "login.empty";
	}
	
	@GetMapping("/api/public/health")
	public boolean health() {
		return true;
	}
	
	@GetMapping("/favicon.ico")
	public String favicon() {
		return "forward:/static/favicon.ico";
	}

}
