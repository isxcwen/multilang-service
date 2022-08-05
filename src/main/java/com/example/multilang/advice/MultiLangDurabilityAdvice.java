package com.example.multilang.advice;

import com.example.multilang.config.MultiLangContext;
import com.example.multilang.model.MultiLangContent;
import com.example.multilang.model.MultiLangModel;
import com.example.multilang.util.MultiLangUtils;
import com.example.service.MultiLangEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiLangDurabilityAdvice implements RequestBodyAdvice {
    private MultiLangContext multiLangContext = MultiLangContext.getContext();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            if(MultiLangUtils.isMultiLangEntity(body.getClass())) {
                List<MultiLangModel> multiLangModels = getMultiLangModels(body);
                multiLangContext.cache(multiLangModels);
            }
        } catch (IllegalAccessException e) {
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private List<MultiLangModel> getMultiLangModels(Object body) throws IllegalAccessException {
        List<MultiLangModel> result = new ArrayList<>();
        Class<?> aClass = body.getClass();
        MultiLangContext.MultiLangFieldCache multiFieldCache = multiLangContext.getMultiFieldCache(aClass);
        Collection<Field> contentField = multiFieldCache.getContentField();
        for (Field field : contentField) {
            result.add(MultiLangModel.init(field, (List<MultiLangContent>) field.get(body)));
        }
        return result;
    }
}
