package com.mediconnect.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SPRING BOOT CONCEPT: API Gateway
 * The gateway is the SINGLE entry point for all clients (mobile app, web app).
 * No client talks directly to patient-service or doctor-service.
 * All traffic flows: Client → Gateway → Correct Microservice
 *
 * Benefits:
 * - One place to handle auth (JWT validation)
 * - One place to handle rate limiting
 * - Services can change ports/addresses without affecting clients
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
