package com.leo.spring.cloud.netflix.zuul.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * JUST TO SHOW HOW DYNAMICALLY ADD DATA TO /actuator/info ENDPOINT
 * visit localhost:port/actuator/info
*/

@Component
public class CustomInfoContributor implements InfoContributor {

    //@Autowired UserRepository userRepository;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Integer> userDetails = new HashMap<>();
        userDetails.put("active", new Random().nextInt());
        userDetails.put("inactive", new Random().nextInt());

        builder.withDetail("users", userDetails);
    }
}