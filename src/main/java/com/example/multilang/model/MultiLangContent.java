package com.example.multilang.model;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.example.service.MultiLangEntity;
import org.apache.ibatis.type.JdbcType;

public class MultiLangContent {
    private String languageCode;
    private String content;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static MultiLangContent init(MultiLangEntity multiLangEntity, JdbcType jdbcType){
        MultiLangContent multiLangContent = new MultiLangContent();
        multiLangContent.setLanguageCode(multiLangEntity.getLanguageCode());
        multiLangContent.setContent(multiLangEntity.realContent(jdbcType));
        return multiLangContent;
    }
}
