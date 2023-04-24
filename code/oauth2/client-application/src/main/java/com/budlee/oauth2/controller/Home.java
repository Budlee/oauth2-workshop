package com.budlee.oauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("home")
public class Home {
	@GetMapping("/")
	public String simpleRequest(){
		return "Welcome home";
	}
}
