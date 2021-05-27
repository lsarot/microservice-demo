package com.leo.spring.cloud.springhystrixrestconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.leo.spring.cloud.springhystrixrestconsumer.withfeignclient.IGreetingController_WithFeignClient;

/**
 * This controller uses one of:
 * - A @Service which uses a RestTemplate/WebClient
 * - A @FeignClient (from package withfeignclient)
 *  to do the requests to the rest-producer app
 *  
 *  Both implements Hystrix as circuit breaker mechanism,
 *  one with @HystrixCommand, one with just a fallback attribute
 * */
@RestController
public class GreetingController {
 
	//WITH RestTemplate/WebClient IN THE SERVICE
    @Autowired private GreetingService greetingService;
 
	
	//WITH FEIGN CLIENT
	//@Autowired IGreetingController_WithFeignClient greetingService;
    
	
    /**
     * How to return template using Thymeleaf
     * Natural Template
     * @throws Exception 
     * */
    @GetMapping("/get-greeting/{username}")
    public ModelAndView getGreeting(@PathVariable("username") String username) throws Exception {
        
    	ModelAndView mav = new ModelAndView("greeting-view");
    	mav.addObject("greeting", greetingService.greeting(username));
        return mav;
    }
    
    
    /**
     * How to return template using Thymeleaf
     * Inlining Expression
     * No usamos th:text en el html
     * @throws Exception 
     * */
    @GetMapping("/get-greeting2/{username}")
    public ModelAndView getGreeting2(@PathVariable("username") String username) throws Exception {
        
    	ModelAndView mav = new ModelAndView("greeting-view2");
    	mav.addObject("greeting", greetingService.greeting(username));
        return mav;
    }
}
