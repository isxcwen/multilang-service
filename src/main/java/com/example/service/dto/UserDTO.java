package com.example.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import com.example.service.Address;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@TableName("user_table")
public class UserDTO implements BaseMultiLang {
    private Long id;

    @TableField(value = "name", jdbcType = JdbcType.VARCHAR)
    private List<MultiLangContent> name;

    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MultiLangContent> getName() {
        return name;
    }

    public void setName(List<MultiLangContent> name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
