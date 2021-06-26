package com.leo.spring.cloud.netflix.zuul.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

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

	//-------------------------------------------

	/**
	 * DEL PLUGIN QUE GENERA EL git.properties
	 * Aparte de configurar en el plugin del pom
	 * Configuramos este Bean para setear que no falle si queremos insertar un property en algún sitio
	*/
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
        propsConfig.setLocation(new ClassPathResource("git.properties"));
        propsConfig.setIgnoreResourceNotFound(true);
        propsConfig.setIgnoreUnresolvablePlaceholders(true);
        return propsConfig;
    }

	//-------------------------------------------

	/**
	 * Extension of actuator endpoint
	 * 
	 * @EndpointWebExtension or @EndpointJmxExtension
	 * */
	@EndpointWebExtension(endpoint = InfoEndpoint.class)
	@Component static class InfoWebEndpointExtension {
		
		@Autowired private InfoEndpoint delegate;
		
		//@Autowired Environment env;
		@Value("${git.build.host}") private String buildHost;
		@Value("${git.total.commit.count}") private String commitCount;
		
		@ReadOperation
		public WebEndpointResponse<Map> info() {
			Map<String, Object> info = this.delegate.info();
			//Integer status = getStatus(info);
			
			Map<String, Object> clone = new HashMap();
			clone.putAll(info);
			//PODEMOS USAR UNA LIBRERÍA DE YAML CM SnakeYaml PARA INSERTAR CON FACILIDAD LOS ITEMS
			((Map)clone.get("git")).put("git.build.host", buildHost);
			((Map)clone.get("git")).put("git.total.commit.count", commitCount);
			
			return new WebEndpointResponse<>(clone, 200);
		}
		/*private Integer getStatus(Map<String, Object> info) {
			// return 5xx if this is a snapshot
			return 200;
		}*/
	}

}
