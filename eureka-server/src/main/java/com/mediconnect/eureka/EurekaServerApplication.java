package com.mediconnect.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * SPRING BOOT CONCEPT: @SpringBootApplication
 * This single annotation does 3 things:
 *  1. @Configuration       - marks this as a config class
 *  2. @EnableAutoConfiguration - Spring Boot auto-configures beans for you
 *  3. @ComponentScan       - scans this package for Spring components
 *
 * SPRING CLOUD CONCEPT: @EnableEurekaServer
 * This turns this simple Spring Boot app into a full Service Registry.
 * All other microservices will register themselves here on startup.
 * Think of it as a "phone book" — services look up each other by name, not hardcoded IPs.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
