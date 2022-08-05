package com.example.multilang.config;

import com.example.multilang.advice.MultiLangDurabilityAdvice;
import com.example.multilang.advice.MultiLangInjectAdvice;
import com.example.multilang.annotations.MultiLangAcvice;
import com.example.multilang.util.MultiLangUtils;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiLangConfiguration {
    //class --> tableName --> columnName - model
    private static Map<Class, MultiLangCache> multiLangColumnCache = new HashMap<>();
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

    public static MultiLangCache getMultiColumn(Class clazz){
        return multiLangColumnCache.get(clazz);
    }

    public static void registerMultiLangColumn(Field field) {
        registerMultiLangColumn(field.getDeclaringClass(), field);
    }

    public static void registerMultiLangNested(Class clazz, Field field) {
        MultiLangCache multiLangCache = multiLangColumnCache.get(clazz);
        MultiLangCache nested = multiLangColumnCache.get(field.getDeclaringClass());
        if(multiLangCache == null){
            multiLangCache = new MultiLangCache();
            multiLangCache.setClazz(clazz);
            multiLangColumnCache.put(clazz, multiLangCache);
        }
        if(nested == null){
            nested = new MultiLangCache();
            nested.setClazz(field.getDeclaringClass());
            multiLangColumnCache.put(field.getDeclaringClass(), nested);
        }
        multiLangCache.addNested(field, nested);
    }

    private static void registerMultiLangColumn(Class clazz, Field field) {
        MultiLangCache multiLangCache = multiLangColumnCache.get(clazz);
        if(multiLangCache == null){
            multiLangCache = new MultiLangCache();
            multiLangCache.setClazz(clazz);
            multiLangColumnCache.put(clazz, multiLangCache);
        }
        multiLangCache.addContentField(field);
    }

    public static class MultiLangCache {
        private Class clazz;

        private Collection<Field> contentField;

        private Map<Field, Collection<MultiLangCache>> nested;

        public MultiLangCache() {
            contentField = new HashSet<>();
        }

        public Class getClazz() {
            return clazz;
        }

        protected void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Collection<Field> getContentField() {
            return contentField;
        }

        public Map<String, Field> getContentFieldMap() {
            return contentField.stream().collect(Collectors.toMap(MultiLangUtils::getColumnName, Function.identity()));
        }

        protected void setContentField(Collection<Field> contentField) {
            this.contentField = contentField;
        }

        protected void addContentField(Field contentField) {
            this.contentField.add(contentField);
        }

        public Map<Field, Collection<MultiLangCache>> getNested() {
            return nested;
        }


        protected void setNested(Map<Field, Collection<MultiLangCache>> nested) {
            this.nested = nested;
        }

        public void addNested(Field field, MultiLangCache multiLangCache) {
            Collection<MultiLangCache> multiLangCaches = this.nested.get(field);
            if(multiLangCaches == null){
                multiLangCaches = new HashSet<>();
                this.nested.put(field, multiLangCaches);
            }
            multiLangCaches.add(multiLangCache);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MultiLangCache that = (MultiLangCache) o;
            return Objects.equals(clazz, that.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz);
        }
    }
}
