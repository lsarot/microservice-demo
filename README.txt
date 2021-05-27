#################### SOURCES:
https://www.adictosaltrabajo.com/2020/12/22/como-crear-y-desplegar-microservicios-con-spring-boot-spring-cloud-netflix-y-docker/
https://www.baeldung.com/spring-cloud-netflix-eureka
and others... is a compilation of some sources!


#################### DESCRIPTION:
Bootstraps:
1.Eureka server (eureka-service) (service registry)
2.Zuul server (zuul-service) (api gateway)
3.A rest client app which exposes an endpoint to retrieve available services (greeting-service) (microservice)
4.A rest client app which comunicates with greeting-service using an http client abstraction called FeignClient (feign-client-service) (microservice)
5.A rest client app which demostrates how to use Hystrix as circuit breaker, this one does not forward requests, but simulates a fail or success. This also uses a FeignClient and a RestTemplate.

* we could have used feign-client-service (#4) to show hystrix usage, but note this app does not use same spring-boot version nor spring-cloud version.

* we show Actuator usage in Zuul server app (Docker-SpringCloudNetflix-ZuulService), algo adding git info to the /info path

Also uses Docker if we want to!


#################### ARCHITECTURE:
//all of them registers on Eureka server, and user can:
//user makes a req to zuul and the last one forwards the req to greeting
//user makes a req to feign-client and the last one forwards req to greeting

                            eureka
user ---- zuul -------------| | |
    |      |                  | |
    |      |                  | |
    |      ------ greeting ---| |
    |               |           |
    |               |           |
    | ---------- feign-client --|
    |
    |----- hystrix


#################### DEPLOY-STEPS:

cd microservice-demo
mvn clean package -Dmaven.test.skip=true //compila todos los módulos

WITHOUT DOCKER:
mvn spring-boot:run //o java -jar target/*.jar   (en cada proyecto)

WITH DOCKER:
docker compose up --build --force-recreate
//or with multiple replicas of a service:
docker compose --file docker-compose-scale.yml up --build
// -d para dejar la terminal libre
//sin docker-compose tendríamos que levantar cada container por separado mapeando puertos, asignando network, env vars, etc.

//Podemos monitorizar el proceso de despliegue desde Docker Dashboard, donde la network que acabamos de crear tendrá todos los logs de los tres servicios.


#################### DEMO USAGE:

VER MAPEO DE RUTAS QUE DETECTA ZUUL:
http://localhost:7000/actuator/routes
EUREKA SERVER:
http://localhost:8761
DIRECTAMENTE A GREETING-MICROSERVICE: (con Docker no exponemos su puerto al host, entonces está bloqueado!)
//port está seteado para que se asigne aleatoriamente!
http://localhost:port/services
http://localhost:port/services-byname/greeting-microservice
http://localhost:port/services-byname/zuul-service
A TRAVÉS DE ZUUL:
http://localhost:7000/greeting-service/services
http://localhost:7000/greeting-service/services-byname/greeting-microservice
http://localhost:7000/greeting-service/services-byname/zuul-service
A TRAVÉS DE FEIGN CLIENT:
http://localhost:8080/get-services
http://localhost:8080/get-services-byname/greeting-microservice
http://localhost:8080/get-services-byname/zuul-service
TEST RATE LIMITING:
Unfortunately, rate limiting is not provided out of the box, so we added support with another dependency.
Requests are configured in application.yml to be forwarded to / (on the same domain).
http://localhost:7000/gw/limiting/simple
http://localhost:7000/gw/limiting/advanced
TEST HYSTRIX CIRCUIT BREAKER:
http://localhost:8090/get-greeting/Leo
http://localhost:8090/get-greeting/FAIL


