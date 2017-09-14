package io.springbox.reviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableResourceServer
@EnableCircuitBreaker
public class SpringboxReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringboxReviewsApplication.class, args);
    }

}
