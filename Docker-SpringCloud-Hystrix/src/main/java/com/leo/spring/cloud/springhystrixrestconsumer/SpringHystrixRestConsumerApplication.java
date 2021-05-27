package com.leo.spring.cloud.springhystrixrestconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableCircuitBreaker // will scan the classpath for any compatible Circuit Breaker implementation.
@SpringBootApplication
public class SpringHystrixRestConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringHystrixRestConsumerApplication.class, args);
	}

}



/**
 * Hystrix uses AOP feature for configuration
 * In case we use another AOP advice such as Spring transactions,
 * we could set the order in which the advices are stacked.
 * */
/* EXAMPLE:
@EnableHystrix
@EnableTransactionManagement(
  order=Ordered.LOWEST_PRECEDENCE, 
  mode=AdviceMode.ASPECTJ)
public class RatingServiceApplication {
    
    @Bean
    @Primary
    @Order(value=Ordered.HIGHEST_PRECEDENCE)
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }
 
    // other beans, configurations
}
*/