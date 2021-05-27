package com.leo.spring.cloud.feignclientmicroservice;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("greeting-microservice")
public interface IGreetingClient extends IGreetingController {

}

/*
Think of Feign as discovery-aware Spring RestTemplate using interfaces to communicate with endpoints. 
This interfaces will be automatically implemented at runtime and instead of service-urls, it is using service-names. (ig. greeting-microservice)

-------------------

Without Feign we would have to autowire an instance of EurekaClient (or DiscoveryClient) into our controller with which we could receive a service-information by service-name as an Application object.
We would use this Application to get a list of all instances of this service, pick a suitable one and use this InstanceInfo to get hostname and port.
With this, we could do a standard request with any http client.
For example:

@Autowired private EurekaClient eurekaClient;

public void doRequest() {
    Application application = eurekaClient.getApplication("greeting-microservice");
    InstanceInfo instanceInfo = application.getInstances().get(0);
    String hostname = instanceInfo.getHostName();
    int port = instanceInfo.getPort();
    // ... then use an http client to do the request...
}

*** A RestTemplate can also be used to access Eureka client-services by name, but this topic is beyond this write-up.

-------------------

A good method to create such Feign Clients is to create interfaces with @RequestMapping annotated methods and put them into a separate module. (ig. IGreetingController)
This way they can be shared between server and client.
On server-side, you can implement them as @Controller,
and on client-side, they can be extended and annotated as @FeignClient.
 */
