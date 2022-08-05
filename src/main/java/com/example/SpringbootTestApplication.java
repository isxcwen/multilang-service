package com.example;

import com.example.multilang.annotations.EnableMultiLang;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableMultiLang(backages = "com.example.service.dto")
@MapperScan("com.example.service.mapper")
public class SpringbootTestApplication {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }
}
