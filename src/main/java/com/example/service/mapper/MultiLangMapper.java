package com.example.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.multilang.model.MultiLangModel;
import com.example.service.MultiLangEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MultiLangMapper extends BaseMapper<MultiLangEntity> {
    void batchInsert(@Param("id") Long id, @Param("list") List<MultiLangModel> multiLangModels);
}
