package com.geo.rcs.modules.admin.menumanagement.service.impl;


import com.geo.rcs.modules.admin.menumanagement.dao.MenuMapper;
import com.geo.rcs.modules.admin.menumanagement.entity.Menu;
import com.geo.rcs.modules.admin.menumanagement.service.MenuService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月7日 上午10:14:41 
 */
@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuMapper menuMapper;


	@Override
	public PageInfo<Menu> findByPage(Menu menu, int many) {

		if (many == 0) {

			PageHelper.startPage(menu.getPageNo(), menu.getPageSize());
		}

		List<Menu> findByPage = menuMapper.findByPage(menu);

		PageInfo<Menu> pageInfo = new PageInfo<>(findByPage);
		return pageInfo;
	}

	@Override
	public void save(Menu menu) {

		if (menu.getParentId() == null) {
			menu.setParentId(0L);
		}
		menuMapper.insertSelective(menu);
		menuMapper.updateByPrimaryKeySelective(menu);
	}

	@Override
	public void updateMenu(Menu menu) {
		menuMapper.updateByPrimaryKeySelective(menu);
	}

	@Override
	public void delete(Long id) {
		menuMapper.deleteByPrimaryKey(id);
	}

}
