package com.example.multilang.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

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
}
