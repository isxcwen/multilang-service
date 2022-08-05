package com.example.multilang.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.example.multilang.model.MultiLangModel;

import java.lang.reflect.Field;
import java.util.*;

public class MultiLangContext {
    private static DefaultIdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
    private static MultiLangContext multiLangContext;
    private static Map<Class, MultiLangFieldCache> multiLangColumnCache;
    private static ThreadLocal<List<MultiLangModel>> multiLangModelCache;
    private MultiLangContext() {
    }


    public static Long gengrateId(){
        return identifierGenerator.nextId(null);
    }

    public static MultiLangContext getContext(){
        if(multiLangContext == null){
            multiLangContext = new MultiLangContext();
            multiLangContext.multiLangColumnCache = new HashMap<>();
            multiLangModelCache = new ThreadLocal<>();
        }
        return multiLangContext;
    }

    public MultiLangFieldCache getMultiFieldCache(Class clazz){
        return multiLangColumnCache.get(clazz);
    }

    public void registerMultiLangColumn(Field field) {
        registerMultiLangColumn(field.getDeclaringClass(), field);
    }

    public void registerMultiLangNested(Class clazz, Field field) {
        MultiLangFieldCache MultiLangFieldCache = multiLangColumnCache.get(clazz);
        MultiLangFieldCache nested = multiLangColumnCache.get(field.getType());
        if(MultiLangFieldCache == null){
            MultiLangFieldCache = new MultiLangFieldCache();
            MultiLangFieldCache.setClazz(clazz);
            multiLangColumnCache.put(clazz, MultiLangFieldCache);
        }
        if(nested == null){
            nested = new MultiLangFieldCache();
            nested.setClazz(field.getType());
            multiLangColumnCache.put(field.getType(), nested);
        }
        MultiLangFieldCache.addNested(field);
    }

    private void registerMultiLangColumn(Class clazz, Field field) {
        MultiLangFieldCache MultiLangFieldCache = multiLangColumnCache.get(clazz);
        if(MultiLangFieldCache == null){
            MultiLangFieldCache = new MultiLangFieldCache();
            MultiLangFieldCache.setClazz(clazz);
            multiLangColumnCache.put(clazz, MultiLangFieldCache);
        }
        MultiLangFieldCache.addContentField(field);
    }

    public void cache(List<MultiLangModel> multiLangModels) {
        multiLangModelCache.set(multiLangModels);
    }

    public List<MultiLangModel> getCache() {
        return multiLangModelCache.get();
    }

    public List<MultiLangModel> removeCache() {
        List<MultiLangModel> cache = getCache();
        multiLangModelCache.remove();
        return cache;
    }

    public static class MultiLangFieldCache {
        private Class clazz;

        private Collection<Field> contentField;

        private Collection<Field> nested;

        public MultiLangFieldCache() {
            contentField = new HashSet<>();
            nested = new HashSet<>();
        }

        protected void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Collection<Field> getContentField() {
            return contentField;
        }

        protected void addContentField(Field contentField) {
            this.contentField.add(contentField);
        }

        public Collection<Field> getNested() {
            return nested;
        }


        protected void setNested(Collection<Field> nested) {
            this.nested = nested;
        }

        public void addNested(Field field) {
            nested.add(field);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MultiLangFieldCache that = (MultiLangFieldCache) o;
            return Objects.equals(clazz, that.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz);
        }
    }
}
