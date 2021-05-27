package com.leo.spring.cloud.netflix.zuul.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_LIMIT;
import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_QUOTA;
import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_REMAINING;
import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_REMAINING_QUOTA;
import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_RESET;
import static java.lang.Integer.parseInt;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties.Policy;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;

/**
 * TESTS PASABAN EN UN PROYECTO APARTE SIN TANTA CONFIGURACIÓN
 * en este intenta conectarse a eureka-server:8761, la url configurada para funcionar con la network hecha en Docker
 * */

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class SpringCloudNetflixZuulServerApplicationTests {

	private static final String SIMPLE_GREETING = "/gw/limiting/simple";
    private static final String ADVANCED_GREETING = "/gw/limiting/advanced";

    @Autowired private TestRestTemplate restTemplate;

    
    //------------------------------------------- DECLARING A BEAN FOR TESTS
    //or a separated class and then @Import(TestConfig.class) in the test class.
    @TestConfiguration
    static class TestConfig {

        /*@Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder()
                    .basicAuthentication("mkyong", "password")
                    .setConnectTimeout(Duration.ofSeconds(5));
        }*/
        
    	
    	/**
    	 * CHANGE KEYS ON RESPONSE HEADER
    	 * */
    	//@Bean (DEACTIVATED)
        public RateLimitKeyGenerator rateLimitKeyGenerator(RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
            return new DefaultRateLimitKeyGenerator(properties, rateLimitUtils) {
                @Override
                public String key(final HttpServletRequest request, final Route route, final Policy policy) {
                    return super.key(request, route, policy) + "_" + request.getMethod();
                }
            };
        }
        
        
        /**
         * RATE CURSOR STORAGE - ERROR HANDLING
         * */
        //@Bean
        public RateLimiterErrorHandler rateLimitErrorHandler() {
            return new DefaultRateLimiterErrorHandler() {
                @Override
                public void handleSaveError(String key, Exception e) {
                    // implementation
                }

                @Override
                public void handleFetchError(String key, Exception e) {
                    // implementation
                }

                @Override
                public void handleError(String msg, Exception e) {
                    // implementation
                }
            };
        }
        
    }
    //-------------------------------------------
    
    
    @Test
	void contextLoads() {
	}
    
    
    /**
     * Request Within the Rate Limit
     */
    @Test
    public void whenRequestNotExceedingCapacity_thenReturnOkResponse() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(SIMPLE_GREETING, String.class);
        
        HttpHeaders headers = response.getHeaders();
        String key = "-rate-limit-application_serviceSimple_127.0.0.1";
        // como configuramos type: -origin   usa   X-RateLimit-Limit-rate-limit-application-{named_client}_{requesting_IP}

        String limit = headers.getFirst(HEADER_LIMIT + key);
        String remaining = headers.getFirst(HEADER_REMAINING + key);
        String reset = headers.getFirst(HEADER_RESET + key);

        assertEquals("5", limit);
        assertEquals(remaining, "4", remaining);
        assertNotNull(reset);
        org.hamcrest.MatcherAssert.assertThat(
                parseInt(reset),
                is(both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(60000)))
        );
        assertEquals(OK, response.getStatusCode());
    }

    /**
     * Request Exceeding the Rate Limit
     * */
    @Test
    public void whenRequestExceedingCapacity_thenReturnTooManyRequestsResponse() throws InterruptedException {
        ResponseEntity<String> response = this.restTemplate.getForEntity(ADVANCED_GREETING, String.class);
        
        HttpHeaders headers = response.getHeaders();
        String key = "-rate-limit-application_serviceAdvanced_127.0.0.1";
        
        assertHeaders(headers, key, false, false);
        
        assertEquals(OK, response.getStatusCode());

        for (int i = 0; i < 2; i++) {
            response = this.restTemplate.getForEntity(ADVANCED_GREETING, String.class);
        }

        headers = response.getHeaders();
        String limit = headers.getFirst(HEADER_LIMIT + key);
        String remaining = headers.getFirst(HEADER_REMAINING + key);
        String reset = headers.getFirst(HEADER_RESET + key);

        assertEquals("1", limit);
        assertEquals("0", remaining);
        assertNotEquals("2000", reset);
        assertEquals(TOO_MANY_REQUESTS, response.getStatusCode());

        TimeUnit.SECONDS.sleep(2);

        response = this.restTemplate.getForEntity(ADVANCED_GREETING, String.class);
        headers = response.getHeaders();
        assertHeaders(headers, key, false, false);
        assertEquals(OK, response.getStatusCode());
    }

    
    private void assertHeaders(HttpHeaders headers, String key, boolean nullable, boolean quotaHeaders) {
        String quota = headers.getFirst(HEADER_QUOTA + key);
        String remainingQuota = headers.getFirst(HEADER_REMAINING_QUOTA + key);
        String limit = headers.getFirst(HEADER_LIMIT + key);
        String remaining = headers.getFirst(HEADER_REMAINING + key);
        String reset = headers.getFirst(HEADER_RESET + key);

        if (nullable) {
            if (quotaHeaders) {
                assertNull(quota);
                assertNull(remainingQuota);
            } else {
                assertNull(limit);
                assertNull(remaining);
            }
            assertNull(reset);
        } else {
            if (quotaHeaders) {
                assertNotNull(quota);
                assertNotNull(remainingQuota);
            } else {
                assertNotNull(limit);
                assertNotNull(remaining);
            }
            assertNotNull(reset);
        }
    }

}
