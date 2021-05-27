package com.leo.spring.cloud.springhystrixrestconsumer;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * For the Circuit Breaker to work, Hystix will scan @Component or @Service annotated classes for @HystixCommand annotated methods, implement a proxy for it and monitor its calls.
 * */

@Service
public class GreetingService {
    
	@HystrixCommand(
			commandKey = "optionalGivenName",
			fallbackMethod = "defaultGreeting"
			/*ignoreExceptions = { SomeException.class }*/
			/*there is a time threshold configurable*/
			//, commandProperties = {
					/*Normally a @HytrixCommand annotated method is executed in a thread pool context. But sometimes it needs to be running in a local scope, for example, a @SessionScope or a @RequestScope*/
					//@com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
					//}
			)
    public String greeting(String username) throws Exception {
		
		if (username.equals("FAIL"))
			throw new Exception("moked fail due to username=FAIL");
		else
			return String.format("Hello %s, this is moked to simulate a successfull response!.. use FAIL to simulate an error and call fallback method!", username);
		
        /* A real usage with RestTemplate
        return new RestTemplate()
        		.getForObject(
        				"http://localhost:9090/greeting/{username}", 
        				String.class,
        				username);*/
    }
	
	
	// fallback method must reside in the same class
    private String defaultGreeting(String username) {
    	System.out.println("fallback for RestTemplate demo");
    	return "Hello User! from fallback method";
    }
}
