package com.example.multilang.advice;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.config.MultiLangConfiguration;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangModel;
import com.example.multilang.service.MultiLangService;
import com.example.multilang.util.MultiLangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiLangInjectAdvice implements ResponseBodyAdvice {
    @Autowired
    @SuppressWarnings("all")
    private MultiLangService multiLangService;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List<BaseMultiLang> data = getData(body);
        if(data != null && !data.isEmpty()){
            Class<? extends BaseMultiLang> aClass = data.get(0).getClass();

            MultiLangConfiguration.MultiLangCache multiLangCache = MultiLangConfiguration.getMultiColumn(aClass);

            String tableName = MultiLangUtils.getTableName(aClass);
            List<MultiLangModel> multiLangByTableIds = multiLangService.getMultiLangByTableIds(tableName, data.stream().map(BaseMultiLang::getId).collect(Collectors.toList()));

            multiLangHandle(data, multiLangByTableIds, multiLangCache);

            Collection<MultiLangConfiguration.MultiLangCache> nested = multiLangCache.getNested();
        }
        return null;
    }

    private List<Long> getIds(Map<String, List<Long>> table2id, String tableName){
        List<Long> longs = table2id.get(tableName);
        if(longs == null){
            longs = new ArrayList<>();
            table2id.put(tableName, longs);
        }
        return longs;
    }

    public List<BaseMultiLang> getData(Object body){
        return null;
    }

    public BaseMultiLang getSigleData(Object body){
        return null;
    }

    public void multiLangHandle(List<BaseMultiLang> baseMultiLangs, List<MultiLangModel> multiLangByTableIds, MultiLangConfiguration.MultiLangCache multiLangCache) {
        if(baseMultiLangs == null || baseMultiLangs.isEmpty()){
            return;
        }
        Map<Long, MultiLangModel> collect = multiLangByTableIds.stream().collect(Collectors.toMap(MultiLangModel::getId, Function.identity()));
        if(collect == null || collect.isEmpty()){
            return;
        }
        Map<String, Field> contentFieldMap = multiLangCache.getContentFieldMap();

        if(contentFieldMap == null || contentFieldMap.isEmpty()){
            return;
        }

        baseMultiLangs.stream().forEach(baseMultiLang -> {
            Long id = baseMultiLang.getId();
            MultiLangModel multiLangModel = collect.get(id);
            if(multiLangCache != null){
                Field field = contentFieldMap.get(multiLangModel.getColumnName());
                try {
                    field.set(baseMultiLang, multiLangModel.getContents());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
