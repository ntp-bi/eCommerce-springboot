package com.ntp.ecommercespringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EntityScan(basePackages = "com.ntp.ecommercespringboot.model")
public class ECommerceSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceSpringbootApplication.class, args);
    }

}
