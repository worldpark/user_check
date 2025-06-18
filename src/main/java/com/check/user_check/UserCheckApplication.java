package com.check.user_check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class UserCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCheckApplication.class, args);

    }

}
