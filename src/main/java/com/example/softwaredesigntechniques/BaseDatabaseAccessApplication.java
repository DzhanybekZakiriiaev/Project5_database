package com.example.softwaredesigntechniques;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BaseDatabaseAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseDatabaseAccessApplication.class, args);
    }

}
