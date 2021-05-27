package com.leo.spring.cloud.greetingmicroservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController implements IGreetingController {

	@Autowired GreetingService greetingService;
	
	
	@Override
    public List<String> getServices() {
        return greetingService.getServices();
    }
	
	
	/**
	 * DIRECT:
	 * http://localhost:8001/services-byname/greeting-microservice
	 * THROUGH API GW:
	 * http://localhost:7000/greeting-service/services-byname/greeting-microservice
	 * */
	@Override
    public List<ServiceInstance> getInstancesByName(@PathVariable("appName") String appName) {
        return greetingService.getInstancesByName(appName);
    }
	
}
