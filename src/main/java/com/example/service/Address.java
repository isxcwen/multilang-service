package com.example.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
@TableName("address_table")
public class Address implements BaseMultiLang {
    private Long id;

    @TableField(value = "name", jdbcType = JdbcType.LONGVARCHAR)
    private List<MultiLangContent> name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
