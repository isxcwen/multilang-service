package com.example.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@TableName("aa")
public class User {
    private Long id;

    @TableField(value = "bb", jdbcType = JdbcType.LONGVARCHAR)
    private List<String> name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
