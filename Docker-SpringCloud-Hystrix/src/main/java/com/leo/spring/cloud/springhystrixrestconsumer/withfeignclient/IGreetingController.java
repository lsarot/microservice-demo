package com.leo.spring.cloud.springhystrixrestconsumer.withfeignclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface IGreetingController {
	
	@GetMapping("/greeting/{username}")
	String greeting(@PathVariable("username") String username);
}
