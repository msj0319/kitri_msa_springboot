package com.kitri.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	private Environment env;
//	@Value("${greeting.name}")
//	String name;
	
	//IoC(제어의 역전) : DL, DI(생성자, set property 메서드)
	@Autowired
	public HomeController(Environment env) {
		this.env = env;
	}
	
	@GetMapping("/health-check")
	public String healthCheck() {
		return String.format(
				"user-service connected"
				+ ", port(server.port) : " + env.getProperty("server.port")
				+ ", welcome : " + env.getProperty("greeting.name")
				);
	}
	
	
}
