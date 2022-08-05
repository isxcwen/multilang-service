package com.example.multilang.model;

import com.example.multilang.util.MultiLangUtils;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.util.List;

public class MultiLangModel {
    private Long id;
    private String tableName;
    private String columnName;
    private Boolean isText;
    private List<MultiLangContent> contents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Boolean getText() {
        return isText;
    }

    public void setText(Boolean text) {
        isText = text;
    }

    public List<MultiLangContent> getContents() {
        return contents;
    }

    public void setContents(List<MultiLangContent> contents) {
        this.contents = contents;
    }

    public static MultiLangModel init(Field field, List<MultiLangContent> contents) {
        MultiLangModel multiLangModel = new MultiLangModel();
        multiLangModel.setTableName(MultiLangUtils.getTableName(field));
        multiLangModel.setColumnName(MultiLangUtils.getColumnName(field));
        multiLangModel.isText = MultiLangUtils.getJdbcType(field) == JdbcType.LONGVARCHAR;
        multiLangModel.contents = contents;
        return multiLangModel;
    }
}
