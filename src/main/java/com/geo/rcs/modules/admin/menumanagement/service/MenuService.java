package com.geo.rcs.modules.admin.menumanagement.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.admin.menumanagement.entity.Menu;
import com.geo.rcs.modules.sys.entity.PageInfo;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月6日 上午10:14:41
 */
public interface MenuService {

	PageInfo<Menu> findByPage(Menu field, int many) throws ServiceException;

	void save(Menu Menu) throws ServiceException;

	void delete(Long id) throws ServiceException;

	void updateMenu(Menu menu) throws ServiceException;

}
