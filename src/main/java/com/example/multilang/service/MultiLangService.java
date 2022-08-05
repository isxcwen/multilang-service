package com.example.multilang.service;


import com.example.multilang.model.MultiLangModel;
import com.example.service.MultiLangEntity;

import java.util.List;

public interface MultiLangService {
    List<MultiLangEntity> getMultiLangByTableId(String tableName, Long tableId);

    List<MultiLangEntity> getMultiLangByTableIds(String tableName, List<Long> tableIds);

    void batchSave(Long id, List<MultiLangModel> multiLangModels);

    void batchDelete(String tableName, Long tableId);
}
