SOURCE:
https://www.baeldung.com/spring-cloud-netflix-hystrix

DESCRIPTION:
In this tutorial we are going to cover the Circuit Breaker pattern using Spring Cloud Hystrix, a strategy against failure cascading, that help make applications resilient, fault and latency tolerant.
Hystrix is watching methods for failing calls to related services. If there is such a failure, it will open the circuit and forward the call to a fallback method.
It will forward calls until a threshold is reached, then subsequent calls will go directly to backup method, to give time to recover.


STEPS:
cd spring-simple-rest-producer
mvn spring-boot:run
cd spring-hystrix-rest-consumer
mvn spring-boot:run
go to 'http://localhost:8080/get-greeting/leo' or 'http://localhost:8080/get-greeting2/leo'
halt spring-simple-rest-producer to force fallback method usage.
then go again to 'http://localhost:8080/get-greeting/leo' or 'http://localhost:8080/get-greeting2/leo'


--------------------------------------------------------------------- To use Hystrix without SpringBoot integration:
https://www.baeldung.com/introduction-to-hystrix

<dependency>
    <groupId>com.netflix.hystrix</groupId>
    <artifactId>hystrix-core</artifactId>
    <version>1.5.4</version>
</dependency>
<dependency>
    <groupId>com.netflix.rxjava</groupId>
    <artifactId>rxjava-core</artifactId>
    <version>0.20.7</version>
</dependency>

class RemoteServiceTestSimulator {

    private long wait;

    RemoteServiceTestSimulator(long wait) throws InterruptedException {
        this.wait = wait;
    }

    String execute() throws InterruptedException {
        Thread.sleep(wait);
        return "Success";
    }
}

//The call to the service is isolated and wrapped in the run() method of a HystrixCommand. Its this wrapping that provides the resilience!

class RemoteServiceTestCommand extends HystrixCommand<String> {

    private RemoteServiceTestSimulator remoteService;

    RemoteServiceTestCommand(Setter config, RemoteServiceTestSimulator remoteService) {
        super(config);
        //or super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        return remoteService.execute();
    }
}

//The call is executed by calling the execute() method on an instance of the RemoteServiceTestCommand object.

@Test
public void test() {

	// *** Defensive Programming With Limited Thread Pool ***

	/*
    Setting timeouts for service call does not solve all the issues associated with remote services.
	When a remote service starts to respond slowly, a typical application will continue to call that remote service.
	The application doesn't know if the remote service is healthy or not and new threads are spawned every time a request comes in. This will cause threads on an already struggling server to be used.
	We don't want this to happen as we need these threads for other remote calls or processes running on our server and we also want to avoid CPU utilization spiking up.
    */

    HystrixCommand.Setter config = HystrixCommand
      .Setter
      .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));

    HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
    properties.withExecutionTimeoutInMilliseconds(10_000);

    config.andCommandPropertiesDefaults(properties);
    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
      .withMaxQueueSize(10)
      .withCoreSize(3)
      .withQueueSizeRejectionThreshold(10));


    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(), equalTo("Success"));


    //*** Defensive Programming With Short Circuit Breaker Pattern ***

    /*
    Let's consider the case that the remote service has started failing.
    We don't want to keep firing off requests at it and waste resources. 
    We would ideally want to stop making requests for a certain amount of time in order to give the service time to recover before then resuming requests.
    This is what is called the Short Circuit Breaker pattern.
    */

    HystrixCommand.Setter config = HystrixCommand
      .Setter
      .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));

    //our HystrixCommand will now trip open after two failed request. The third request will not even hit the remote service even though we have set the service delay to be 500 ms, Hystrix will short circuit and our method will return null as the response.
    HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
    properties.withExecutionTimeoutInMilliseconds(1000);
    properties.withCircuitBreakerSleepWindowInMilliseconds(4000); //time to recover
    properties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
    properties.withCircuitBreakerEnabled(true);
    properties.withCircuitBreakerRequestVolumeThreshold(1); //minimum number of requests needed before the failure rate will be considered

    config.andCommandPropertiesDefaults(properties);
    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
      .withMaxQueueSize(1)
      .withCoreSize(1)
      .withQueueSizeRejectionThreshold(1));

    assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));

    Thread.sleep(5000);

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
      equalTo("Success"));

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
      equalTo("Success"));

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
      equalTo("Success"));
}

public String invokeRemoteService(HystrixCommand.Setter config, int timeout) throws InterruptedException {
    String response = null;
    try {
        response = new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(timeout)).execute();
    } catch (HystrixRuntimeException ex) { System.out.println("ex = " + ex); }
    return response;
}




