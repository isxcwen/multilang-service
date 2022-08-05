package com.example.multilang.config;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.annotations.EnableMultiLang;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegisterMultiLangModel implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scaner = new ClassPathScanningCandidateComponentProvider(false);
        scaner.addIncludeFilter(new AssignableTypeFilter(BaseMultiLang.class));
        scaner.addIncludeFilter(new AnnotationTypeFilter(TableName.class));

        String[] scanNackages = (String[]) importingClassMetadata.getAnnotationAttributes(EnableMultiLang.class.getName()).get("scanBackages");
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (String scanNackage : scanNackages) {
            beanDefinitions.addAll(scaner.findCandidateComponents(scanNackage));
        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> aClass;
            try {
                aClass = Class.forName(beanClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            regist(aClass);
        }
    }

    private void regist(Class<?> aClass){
        ReflectionUtils.doWithFields(aClass, field -> {
            if(isMultiLangColumn(field)){
                field.setAccessible(true);
                MultiLangConfiguration.registerMultiLangColumn(field);
            }else if(isMultiLangEntity(field)){
                field.setAccessible(true);
                regist(field.getDeclaringClass());
                MultiLangConfiguration.registerMultiLangNested(aClass, field);
            }
        });
    }

    private boolean isMultiLangColumn(Field field){
        return field.isAnnotationPresent(TableField.class) && isLangContent(field);
    }

    private boolean isMultiLangEntity(Field field){
        Class<?> declaringClass = field.getDeclaringClass();
        return BaseMultiLang.class.isAssignableFrom(declaringClass) && declaringClass.isAnnotationPresent(TableName.class);
    }

    private boolean isLangContent(Field field){
        if(List.class.isAssignableFrom(field.getType())){
            Type genericType = field.getGenericType();
            if(genericType instanceof ParameterizedType){
                Type actualTypeArgument = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                return MultiLangContent.class.equals(actualTypeArgument);
            }
        }
        return false;
    }
}
