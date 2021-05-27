package com.leo.spring.cloud.springhystrixrestconsumer;

import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;

@EnableHystrixDashboard
@Configuration
public class HystrixDashboardConfig {
	
	/**
	 * Point the browser to http://localhost:port/hystrix
	 * 
	 * Monitoring a Hystrix stream is something fine, but if we have to watch multiple Hystrix-enabled applications, it will become inconvenient. For this purpose, Spring Cloud provides a tool called Turbine, which can aggregate streams to present in one Hystrix dashboard.
	 * So it's also possible to collect these streams via messaging, using Turbine stream.
	 * */

}
