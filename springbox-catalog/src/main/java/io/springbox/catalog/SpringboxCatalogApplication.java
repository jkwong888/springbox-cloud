package io.springbox.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCircuitBreaker
@EnableJpaRepositories
@EnableDiscoveryClient
public class SpringboxCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringboxCatalogApplication.class, args);
    }
}
