package com.example.multilang.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class MultiLangUtils {

    public static String getTableName(Class clazz){
        return ((TableName) clazz.getAnnotation(TableName.class)).value();
    }

    public static String getTableName(Field field){
        return getTableName(field.getDeclaringClass());
    }

    public static String getColumnName(Field field){
        return field.getAnnotation(TableField.class).value();
    }

    public static JdbcType getJdbcType(Field field){
        return field.getAnnotation(TableField.class).jdbcType();
    }

    public static boolean isMultiLangColumn(Field field){
        return field.isAnnotationPresent(TableField.class) && isLangContent(field);
    }

    public static  boolean isMultiLangEntity(Field field){
        Class<?> type = field.getType();
        return isMultiLangEntity(type);
    }

    public static  boolean isMultiLangEntity(Class type){
        return BaseMultiLang.class.isAssignableFrom(type) && type.isAnnotationPresent(TableName.class);
    }

    private static  boolean isLangContent(Field field){
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
