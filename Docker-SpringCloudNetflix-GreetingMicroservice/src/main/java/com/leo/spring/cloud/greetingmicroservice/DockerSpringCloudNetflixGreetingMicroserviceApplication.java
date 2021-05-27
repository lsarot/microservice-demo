package com.leo.spring.cloud.greetingmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient activates an implementation of service discovery client (@EnableEurekaClient strictly for Netflix Eureka DiscoveryClient implementation)... (There are other implementations for other service registries, such as Hashicorp’s Consul or Apache Zookeeper).
	//note that this annotation is optional if we have the spring-cloud-starter-netflix-eureka-client dependency on the classpath.
@SpringBootApplication
public class DockerSpringCloudNetflixGreetingMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerSpringCloudNetflixGreetingMicroserviceApplication.class, args);
	}

}
