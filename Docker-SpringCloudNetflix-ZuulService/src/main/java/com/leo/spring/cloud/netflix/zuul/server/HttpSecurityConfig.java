package com.leo.spring.cloud.netflix.zuul.server;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
        .inMemoryAuthentication().passwordEncoder(encoder)
          .withUser("user").password(encoder.encode("password")).roles("USER")
          .and()
          .withUser("admin").password(encoder.encode("admin")).roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        //.pathMatchers("/actuator/**").permitAll()
        .requestMatcher(EndpointRequest.toAnyEndpoint())
          .authorizeRequests((requests) -> requests.anyRequest()
            .hasRole("ADMIN"));
        http.httpBasic();
    }
}