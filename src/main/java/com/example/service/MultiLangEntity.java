package com.example.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;


@TableName("svc_lang_code")
public class MultiLangEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private String tableName;
    private Long tableId;
    private String columnName;
    private String languageCode;
    private String contentVarchar;
    private String contentText;

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

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getContentVarchar() {
        return contentVarchar;
    }

    public void setContentVarchar(String contentVarchar) {
        this.contentVarchar = contentVarchar;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String realContent(JdbcType jdbcType) {
        if(jdbcType == JdbcType.LONGVARCHAR){
            return contentText;
        }
        return contentVarchar;
    }
}
