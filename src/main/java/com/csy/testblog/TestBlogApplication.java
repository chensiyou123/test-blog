package com.csy.testblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestBlogApplication.class, args);
    }

}

