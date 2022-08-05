package com.example.multilang.config;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.annotations.EnableMultiLang;
import com.example.multilang.model.BaseMultiLang;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.example.multilang.util.MultiLangUtils.isMultiLangColumn;

public class RegisterMultiLangModel implements ImportBeanDefinitionRegistrar {
    private MultiLangContext multiLangContext = MultiLangContext.getContext();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scaner = new ClassPathScanningCandidateComponentProvider(false);
        TypeFilter typeFilter = new TypeFilter() {
            AssignableTypeFilter assignableTypeFilter = new AssignableTypeFilter(BaseMultiLang.class);
            AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(TableName.class);
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return assignableTypeFilter.match(metadataReader, metadataReaderFactory) && annotationTypeFilter.match(metadataReader, metadataReaderFactory);
            }
        };
        scaner.addIncludeFilter(typeFilter);

        String[] scanNackages = (String[]) importingClassMetadata.getAnnotationAttributes(EnableMultiLang.class.getName()).get("backages");
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
                multiLangContext.registerMultiLangColumn(field);
            }/*else if(isMultiLangEntity(field)){
                field.setAccessible(true);
                regist(field.getType());
                multiLangContext.registerMultiLangNested(aClass, field);
            }*/
        });
    }
}
