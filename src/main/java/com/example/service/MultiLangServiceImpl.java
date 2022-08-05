package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.multilang.model.MultiLangModel;
import com.example.multilang.service.MultiLangService;
import com.example.service.mapper.MultiLangMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MultiLangServiceImpl implements MultiLangService {
    @Autowired
    private MultiLangMapper multiLangMapper;

    @Override
    public List<MultiLangEntity> getMultiLangByTableId(String tableName, Long tableId) {
        LambdaQueryWrapper<MultiLangEntity> multiLangEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        multiLangEntityLambdaQueryWrapper.eq(MultiLangEntity::getTableName, tableName).eq(MultiLangEntity::getTableId, tableId);
        return multiLangMapper.selectList(multiLangEntityLambdaQueryWrapper);
    }

    @Override
    public List<MultiLangEntity> getMultiLangByTableIds(String tableName, List<Long> tableIds) {
        LambdaQueryWrapper<MultiLangEntity> multiLangEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        multiLangEntityLambdaQueryWrapper.eq(MultiLangEntity::getTableName, tableName).in(MultiLangEntity::getTableId, tableIds);
        return multiLangMapper.selectList(multiLangEntityLambdaQueryWrapper);
    }

    @Override
    public void batchSave(Long id, List<MultiLangModel> multiLangModels) {
        multiLangMapper.batchInsert(id, multiLangModels);
    }

    @Override
    public void batchDelete(String tableName, Long tableId) {
        LambdaQueryWrapper<MultiLangEntity> multiLangEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        multiLangEntityLambdaQueryWrapper.eq(MultiLangEntity::getTableName, tableName).eq(MultiLangEntity::getTableId, tableId);
        multiLangMapper.delete(multiLangEntityLambdaQueryWrapper);
    }
}
