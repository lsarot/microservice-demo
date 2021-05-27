package com.leo.spring.cloud.netflix.zuul.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Just to show how we can restrict requests up to a limit
 * In this case we do not forward req to other service
 * */

@RestController
public class ApiGatewayOwnController {

	@GetMapping("/gw/limiting/simple")
    public ResponseEntity<String> getSimple() {
        return ResponseEntity.ok("Hi!");
    }

    @GetMapping("/gw/limiting/advanced")
    public ResponseEntity<String> getAdvanced() {
        return ResponseEntity.ok("Hello, how you doing?");
    }
    
    @GetMapping("/")
    public ResponseEntity<String> rootPath() {
        return ResponseEntity.ok("HELLO FROM ROOT PATH   /");
    }
	
}
