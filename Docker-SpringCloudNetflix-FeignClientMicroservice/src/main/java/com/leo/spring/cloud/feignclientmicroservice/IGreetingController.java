package com.leo.spring.cloud.feignclientmicroservice;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;

public interface IGreetingController {
	@GetMapping("/services") List<String> getServices();
	@GetMapping("/services-byname/{appName}") List<ServiceInstance> getInstancesByName(String appName);
}
