package com.example;

import com.example.multilang.annotations.EnableMultiLang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableMultiLang(backages = "com.example.service")
public class SpringbootTestApplication implements ApplicationRunner {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
