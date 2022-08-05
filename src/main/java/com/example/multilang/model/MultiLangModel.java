package com.example.multilang.model;

import java.util.List;

public class MultiLangModel {
    private Long id;
    private String tableName;
    private String columnName;
    List<MultiLangContent> contents;

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

    public List<MultiLangContent> getContents() {
        return contents;
    }

    public void setContents(List<MultiLangContent> contents) {
        this.contents = contents;
    }
}
