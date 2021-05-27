package com.leo.spring.cloud.feignclientmicroservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Se usa una abstracción para el HTTP client, FeignClient
 * ig. Hacemos un req a /get-services, se usa el Feign client que apunta a greeting-microservice y se hace el req a /services
 * Los datos de conexión los obtiene de Eureka, al ser todos discovery clients.
 * */

@RestController
public class FeignClientController {

	@Autowired private IGreetingClient greetingClient;
	
	
	@GetMapping("/get-services")
    public ModelAndView getServices(Model model) {
        
		ModelAndView mav = new ModelAndView("greeting-view");
    	mav.addObject("greeting", greetingClient.getServices());
        return mav;
    }
	
	
	@GetMapping("/get-services-byname/{appName}")
    public List<ServiceInstance> getInstancesByName(@PathVariable("appName") String appName) {
        return greetingClient.getInstancesByName(appName);
    }
}
