package com.leo.spring.cloud.springhystrixrestconsumer.withfeignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Spring Netflix Feign as declarative REST client, instead of Spring RestTemplate.
 * The advantage is that we’re later able to easily refactor our Feign Client interface to use Spring Netflix Eureka for service discovery.
 * 
 * We extend the interface and declare it as a Feign Client
 * 
 * The name property of the @FeignClient is mandatory. It is used, to look-up the application either by service discovery via a Eureka Client or by URL, if this property is given.
 * */

@FeignClient(
		  name = "rest-producer",
		  url = "http://localhost:9090", /*url = "${feign-url-from-application.yml}"*/
		  fallback = IGreetingController_WithFeignClient.GreetingClientFallback.class
		)
public interface IGreetingController_WithFeignClient extends IGreetingController {

	
	/**
	 * We'll implement Hystrix fallback as an inner class annotated with @Component
	 * Alternatively, we could define a @Bean annotated method returning an instance of this fallback class, and not using @Component
	 * */
	@Component
	class GreetingClientFallback implements IGreetingController_WithFeignClient {

	    @Override
	    public String greeting(@PathVariable("username") String username) {
	        System.out.println("fallback for FeignClient demo");
	    	return "Hello User! from fallback method";
	    }
	}
	
}
