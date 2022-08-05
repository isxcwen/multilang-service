package com.example.multilang.annotations;

import com.example.multilang.config.MultiLangConfiguration;
import com.example.multilang.config.RegisterMultiLangModel;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({MultiLangConfiguration.class, RegisterMultiLangModel.class})
public @interface EnableMultiLang {
    String[] backages();
}

/**
 * CREATE TABLE svc_language_text
 * (
 *     id                     BIGINT(20) unsigned NOT NULL,
 *     table_name             VARCHAR(255) NOT NULL,
 *     table_id               BIGINT(20) unsigned NOT NULL,
 *     language_code          VARCHAR(255) NOT NULL,
 *     column_name            VARCHAR(255) NOT NULL,
 *     column_content_text    TEXT,
 *     column_content_varchar VARCHAR(255),
 *     PRIMARY KEY (id)
 * ) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '大世界公告跑马灯';
 * alter table svc_language_text
 *     add index idx_language_text_table (table_name, table_id);
 */