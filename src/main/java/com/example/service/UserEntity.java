package com.example.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.multilang.model.BaseMultiLang;
import lombok.Getter;
import lombok.Setter;


@TableName("user")
@Getter
@Setter
public class UserEntity implements BaseMultiLang {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private String name;

}
