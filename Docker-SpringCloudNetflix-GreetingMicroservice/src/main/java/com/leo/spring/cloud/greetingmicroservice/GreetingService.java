package com.leo.spring.cloud.greetingmicroservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
//import com.netflix.discovery.EurekaClient;

@Service
public class GreetingService {
	
	
	private int port;
	// -------------------------------- Getting Tomcat port on runtime 
	//---- with fixed port
	//@LocalServerPort private int port; // fetches local.server.port
	//@Autowired Environment env;   env.getProperty("server.port");
	//@Value("${server.port}") private int port;
	//@Autowired private ServerProperties serverProperties;   serverProperties.getPort()
	//---- with random port
	//@Autowired private ServletWebServerApplicationContext webServerAppCtxt;   webServerAppCtxt.getWebServer().getPort()
	@EventListener public void onApplicationEvent(final ServletWebServerInitializedEvent event) { port = event.getWebServer().getPort(); }
	// --------------------------------
	
	
	@Autowired private DiscoveryClient discoveryClient;
	//@Autowired private EurekaClient eurekaClient;
	
	
	public List<String> getServices() {        
		//String returnValue = new StringBuilder().append("Hello from port: ").append(port).append("n").toString();
		
		//return String.format("Hello from '%s'!", eurekaClient.getApplication("appName").getName());
        
		List<String> svr = this.discoveryClient.getServices();
        svr.add(0, "Requested from Greeting Microservice on port:" + port);
		return svr;
	}
	
	
	public List<ServiceInstance> getInstancesByName(String appName) {
		return this.discoveryClient.getInstances(appName);
	}
	
}
