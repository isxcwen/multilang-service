package com.example.multilang.config;

import com.example.multilang.advice.MultiLangDurabilityAdvice;
import com.example.multilang.advice.MultiLangInjectAdvice;
import com.example.multilang.advice.MultiLangStoreInterceptor;
import com.example.multilang.annotations.MultiLangAcvice;
import com.example.multilang.util.MultiLangUtils;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiLangConfiguration {
    private MultiLangContext multiLangContext = MultiLangContext.getContext();

    @Bean
    @MultiLangAcvice
    public MultiLangInjectAdvice multingualInjectAdvice() {
        return new MultiLangInjectAdvice();
    }

    @Bean
    @MultiLangAcvice
    public MultiLangDurabilityAdvice multingualDurabilityAdvice() {
        return new MultiLangDurabilityAdvice();
    }

    @Bean
    public MultiLangStoreInterceptor multiLangStoreInterceptor() {
        return new MultiLangStoreInterceptor();
    }
}
