
SpringBoot 2.x Actuator endpoints:

/auditevents lists security audit-related events such as user login/logout. Also, we can filter by principal or type among other fields.
/beans returns all available beans in our BeanFactory. Unlike /auditevents, it does not support filtering.
/conditions, formerly known as /autoconfig, builds a report of conditions around autoconfiguration.
/configprops allows us to fetch all @ConfigurationProperties beans.
/env returns the current environment properties. Additionally, we can retrieve single properties.
/flyway provides details about our Flyway database migrations.
/health summarizes the health status of our application.
/heapdump builds and returns a heap dump from the JVM used by our application.
/info returns general information. It might be custom data, build information or details about the latest commit.
/liquibase behaves like /flyway but for Liquibase.
/logfile returns ordinary application logs.
/loggers enables us to query and modify the logging level of our application.
/metrics details metrics of our application. This might include generic metrics as well as custom ones.
/prometheus returns metrics like the previous one, but formatted to work with a Prometheus server.
/scheduledtasks provides details about every scheduled task within our application.
/sessions lists HTTP sessions given we are using Spring Session.
/shutdown performs a graceful shutdown of the application.
/threaddump dumps the thread information of the underlying JVM.


----- CUSTOM HEALTH INDICATORS
https://www.baeldung.com/spring-boot-actuators#health-indicators
@Component public class MyDownstreamServiceHealthIndicator implements ReactiveHealthIndicator {
    @Override public Mono<Health> health() { return Mono.just(new Health.Builder().down(ex).build()); }
}

----- HEALTH GROUPS
https://www.baeldung.com/spring-boot-actuators#6-health-groups

----- METRICS IN SPRINGBOOT 2
https://www.baeldung.com/spring-boot-actuators#7-metrics-in-spring-boot-2
https://www.baeldung.com/micrometer
GaugeService or CounterService no longer supported, now MicrometerService.
In Spring Boot 2.0, we will get a bean of type MeterRegistry autoconfigured for us.
/actuator/metrics to check them all
then ig. /actuator/metrics/jvm.gc.pause

----- CUSTOMIZING THE /info ENDPOINT - add git and build info
https://www.baeldung.com/spring-boot-actuators#info-endpoint
https://www.baeldung.com/spring-git-information
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
</plugin>
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>

----- CREATE A CUSTOM ENDPOINT
https://www.baeldung.com/spring-boot-actuators#custom-endpoint
We can create endpoints mapped to CRUD ops, as GET POST PUT DELETE
to have something like /actuator/feature/{param} that works with those HTTP methods.

----- EXTEND EXISTING ENDPOINTS
https://www.baeldung.com/spring-boot-actuators#extending-endpoints
Let us imagine we want to make sure the production instance of our application is never a SNAPSHOT version.
We decide to do this by changing the HTTP status code of the Actuator endpoint that returns this information,
i.e., /info. If our app happened to be a SNAPSHOT, we would get a different HTTP status code.
We can easily extend the behavior of a predefined endpoint using the @EndpointExtension annotations, or its more concrete specializations @EndpointWebExtension or @EndpointJmxExtension:
@EndpointWebExtension(endpoint = InfoEndpoint.class)
@Component public class InfoWebEndpointExtension {
    private InfoEndpoint delegate;
    // standard constructor
    @ReadOperation
    public WebEndpointResponse<Map> info() {
        Map<String, Object> info = this.delegate.info();
        Integer status = getStatus(info);
        return new WebEndpointResponse<>(info, status);
    }
    private Integer getStatus(Map<String, Object> info) {
        // return 5xx if this is a snapshot
        return 200;
    }
}

