package io.quarkus.it.spring.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    @Secured("admin")
    public Greeting greet(String message) {
        return new Greeting(message);
    }
}
