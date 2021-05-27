package com.leo.spring.cloud.netflix.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Client-side service discovery allows services to find and communicate with each other without hard-coding hostname and port. The only fixed point in such an architecture consists of a service registry with which each service has to register.
A drawback is that all clients must implement a certain logic to interact with this fixed point. This assumes an additional network round trip before the actual request.
	NO NECESARIAMENTE, LOS CLIENTES DEL SERVICE REGISTRY TIENEN ACTIVADO FETCH-REGISTRY PARA REFRESCAR CADA 30s LOS NODOS DISPONIBLES.
With Netflix Eureka each client can simultaneously act as a server, to replicate its status to a connected peer. In other words, a client retrieves a list of all connected peers of a service registry and makes all further requests to any other services through a load-balancing algorithm.
	EL LOAD BALANCER LO IMPLEMENTA ZUUL COMO API GATEWAY, USANDO RIBBON PQ ZUUL ES SÓLO EL PROXY SERVICE, TENDRÍAN QUE USAR RIBBON LOS OTROS CLIENTES PARA DECIDIR CÚAL DE LOS NODOS 'TIPO A', POR EJEMPLO, PUEDEN RESOLVER LA SOLICITUD.
To be informed about the presence of a client, they have to send a heartbeat signal to the registry.

By using the Eureka dashboard, we can do further configuration e.g. link the homepage of a registered client with the Dashboard for administrative purposes.
 * */

@EnableEurekaServer
@SpringBootApplication
public class SpringCloudNetflixEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudNetflixEurekaServerApplication.class, args);
	}

}
