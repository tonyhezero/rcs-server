package com.geo.rcs.modules.rule.business.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 11:49 2019/3/12
 */
public interface BusinessService {
    Page<BusinessType> findByPage(BusinessType business) throws ServiceException;

    List<BusinessType> queryBusinessName(String name, Long uniqueCode);

    void save(BusinessType business)throws ServiceException;

    BusinessType getBusinessById(Long id);

    void updateBusiness(BusinessType business) throws ServiceException;
}
