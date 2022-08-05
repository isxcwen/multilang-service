package com.example.multilang.annotations;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestControllerAdvice
public @interface MultiLangAcvice {
}