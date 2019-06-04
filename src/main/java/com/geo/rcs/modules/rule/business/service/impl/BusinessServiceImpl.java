package com.geo.rcs.modules.rule.business.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.modules.rule.business.dao.EngineBusinessMapper;
import com.geo.rcs.modules.rule.business.service.BusinessService;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 11:50 2019/3/12
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private EngineBusinessMapper businessMapper;

    @Override
    public Page<BusinessType> findByPage(BusinessType business) throws ServiceException {
        PageHelper.startPage(business.getPageNo(),business.getPageSize());
        return businessMapper.findByPage(business);
    }

    @Override
    public List<BusinessType> queryBusinessName(String name, Long uniqueCode) {
        if (BlankUtil.isBlank(name) || BlankUtil.isBlank(uniqueCode)){
            return null;
        }
        return businessMapper.queryBusinessName(name,uniqueCode);
    }

    @Override
    public void save(BusinessType business) {
        business.setAddTime(new Date());
        businessMapper.insertSelective(business);
    }

    @Override
    public BusinessType getBusinessById(Long id) {
        return businessMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateBusiness(BusinessType business) throws ServiceException {
        businessMapper.updateByPrimaryKeySelective(business);
    }
}
