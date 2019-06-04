package com.geo.rcs.modules.source.verified.client.service.impl;

import com.geo.rcs.modules.source.verified.client.dao.VerfiedLogMapper;
import com.geo.rcs.modules.source.verified.client.entity.VerfiedLog;
import com.geo.rcs.modules.source.verified.client.service.VerfiedLogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by geo on 2019/3/29.
 */
@Service
public class VerfiedLogServiceImpl implements VerfiedLogService {
    @Autowired
    private VerfiedLogMapper verfiedLogMapper;
    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(VerfiedLog record) {
        return 0;
    }

    @Override
    public int insertSelective(VerfiedLog record) {
        return verfiedLogMapper.insertSelective(record);
    }

    @Override
    public VerfiedLog selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public List<VerfiedLog> selectAll(VerfiedLog record) {
        return verfiedLogMapper.selectAll(record);
    }

    @Override
    public int updateByPrimaryKeySelective(VerfiedLog record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(VerfiedLog record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(VerfiedLog record) {
        return 0;
    }

    @Override
    public Page<Map<String, Object>> findByPageIds(Long[] ids) {
        return verfiedLogMapper.findByPageIds(ids);
    }

    @Override
    public Page<Map<String, Object>> findByPageAll(VerfiedLog record) {
        return verfiedLogMapper.findByPageAll(record);
    }
}
