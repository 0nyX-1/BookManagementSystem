package org.sohan.BookManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing(/*Providing bean for Auditor Aware*/ auditorAwareRef = "auditorAware")//enable to enable JPA auditing
@EnableAsync
@EnableWebSecurity
@EnableWebMvc
public class SpringSecurity02Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity02Application.class, args);
    }

}
