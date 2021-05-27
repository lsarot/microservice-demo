package com.leo.spring.cloud.feignclientmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class SpringCloudNetflixFeignClientMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudNetflixFeignClientMicroserviceApplication.class, args);
	}
}
