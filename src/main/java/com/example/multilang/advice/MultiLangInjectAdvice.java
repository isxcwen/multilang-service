package com.example.multilang.advice;

import com.example.multilang.config.MultiLangContext;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import com.example.multilang.service.MultiLangService;
import com.example.multilang.util.MultiLangUtils;
import com.example.service.MultiLangEntity;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MultiLangInjectAdvice implements ResponseBodyAdvice {
    @Autowired(required = false)
    @SuppressWarnings("all")
    private MultiLangService multiLangService;
    private MultiLangContext multiLangContext = MultiLangContext.getContext();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //todo 是否要国际化 一般根据通用响应.getData
        try {
            Object data = getData(body);
            if(data instanceof List){
                multiLangHandleList((List<BaseMultiLang>) data);
            }else if (data instanceof BaseMultiLang) {
                multiLangHandle((BaseMultiLang)data);
            }
            return data;
        } catch (IllegalAccessException e) {
        }
        multiLangContext.removeCache();
        return body;
    }

    public Object getData(Object body){
        return body;
    }


    public void multiLangHandle(BaseMultiLang baseMultiLang) throws IllegalAccessException {
        Long id = baseMultiLang.getId();
        Class<? extends BaseMultiLang> aClass = baseMultiLang.getClass();
        String tableName = MultiLangUtils.getTableName(aClass);
        MultiLangContext.MultiLangFieldCache multiFieldCache = multiLangContext.getMultiFieldCache(aClass);
        List<MultiLangEntity> multiLangByTableId = multiLangService.getMultiLangByTableId(tableName, id);
        multiLangHandle(baseMultiLang, multiLangByTableId, multiFieldCache);
    }

    public void multiLangHandle(BaseMultiLang baseMultiLang, List<MultiLangEntity> multiLangByTableIds, MultiLangContext.MultiLangFieldCache multiLangFieldCache) throws IllegalAccessException {
        Collection<Field> contentField = multiLangFieldCache.getContentField();
        Map<String, List<MultiLangEntity>> columnName2MultiLangModel = multiLangByTableIds.stream().collect(Collectors.groupingBy(MultiLangEntity::getColumnName, Collectors.toList()));
        for (Field field : contentField) {
            List<MultiLangEntity> multiLangEntities = columnName2MultiLangModel.get(MultiLangUtils.getColumnName(field));
            JdbcType jdbcType = MultiLangUtils.getJdbcType(field);
            List<MultiLangContent> multiLangContents = multiLangEntities.stream().map(multiLangEntitie -> MultiLangContent.init(multiLangEntitie, jdbcType)).collect(Collectors.toList());
            field.set(baseMultiLang, multiLangContents);
        }

        Collection<Field> nested = multiLangFieldCache.getNested();
        for (Field field : nested) {
            Class<?> type = field.getType();
            if(BaseMultiLang.class.isAssignableFrom(type)){
                BaseMultiLang nestedBaseMultiLang = (BaseMultiLang) field.get(baseMultiLang);
                multiLangHandle(nestedBaseMultiLang);
            }

        }
    }

    public void multiLangHandleList(List<BaseMultiLang> baseMultiLangs) throws IllegalAccessException {
        Class<? extends BaseMultiLang> aClass = baseMultiLangs.get(0).getClass();
        List<Long> ids = baseMultiLangs.stream().map(BaseMultiLang::getId).collect(Collectors.toList());
        String tableName = MultiLangUtils.getTableName(aClass);
        MultiLangContext.MultiLangFieldCache multiFieldCache = multiLangContext.getMultiFieldCache(aClass);
        List<MultiLangEntity> multiLangByTableIds = multiLangService.getMultiLangByTableIds(tableName, ids);
        multiLangHandleList(baseMultiLangs, multiLangByTableIds, multiFieldCache);
    }
    
    public void multiLangHandleList(List<BaseMultiLang> baseMultiLangs, List<MultiLangEntity> multiLangByTableIds, MultiLangContext.MultiLangFieldCache MultiLangFieldCache) throws IllegalAccessException {
        Map<Long, List<MultiLangEntity>> id2MultiLangByTableIds = multiLangByTableIds.stream().collect(Collectors.groupingBy(MultiLangEntity::getTableId));
        for (BaseMultiLang baseMultiLang : baseMultiLangs) {
            List<MultiLangEntity> multiLangEntities = id2MultiLangByTableIds.get(baseMultiLang.getId());
            multiLangHandle(baseMultiLang, multiLangEntities, MultiLangFieldCache);
        }
    }
}
