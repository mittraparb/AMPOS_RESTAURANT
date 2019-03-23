package com.ampos.restaurant.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.ampos.restaurant.gateway")
@EnableAutoConfiguration
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
