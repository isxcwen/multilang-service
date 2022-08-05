package com.example.multilang.service;


import com.example.multilang.model.MultiLangModel;

import java.util.List;

public interface MultiLangService {
    List<MultiLangModel> getMultiLangById(Long id);

    List<MultiLangModel> getMultiLangByIds(List<Long> ids);

    List<MultiLangModel> getMultiLangByTableId(String tableName, Long id);

    List<MultiLangModel> getMultiLangByTableIds(String tableName, List<Long> ids);

    List<MultiLangModel> getMultiLangByTableColumnId(String tableName, String column, Long id);

    List<MultiLangModel> getMultiLangByTableColumnIds(String tableName, String column,List<Long> ids);
}
