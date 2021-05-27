
# Una distribución de Linux extremadamente ligera y un jdk para ejecutar aplicaciones Java.
#FROM openjdk:8-jdk-alpine
FROM openjdk:11-jre-slim

# Para crear un directorio en el que alojaremos el ejecutable de cada una de nuestras aplicaciones.
ADD target/Docker-SpringCloudNetflix-Eureka-Server-0.0.1-SNAPSHOT.jar app.jar
#COPY también sirve!

# Comando que se ejecute cuando se levante el contenedor con esta imagen, el cuál ejecutará nuestra aplicación usando el fichero .jar.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#-Djava.security... no parece necesario!

# Notar la ausencia de la propiedad EXPOSE, que nos permite exponer un puerto en concreto al exterior. Usaremos una network para que los contenedores se comuniquen entre sí.
