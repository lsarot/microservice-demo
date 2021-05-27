package com.leo.spring.cloud.netflix.zuul.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

//@EnableDiscoveryClient   // tener la dependencia eureka-client ya hace que conecte con Eureka server. Con esto quizá podemos inyectar la instancia de DiscoveryClient.
@EnableZuulProxy
@SpringBootApplication(exclude = { 
    SecurityAutoConfiguration.class, 
    ManagementWebSecurityAutoConfiguration.class //for actuator endpoints
})
public class SpringCloudNetflixZuulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudNetflixZuulServerApplication.class, args);
	}
	
	
	@Bean
	  public CustomZuulFilter simpleFilter() {
	    return new CustomZuulFilter();
	  }
}
