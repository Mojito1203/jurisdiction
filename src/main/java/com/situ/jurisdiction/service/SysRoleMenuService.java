package com.situ.jurisdiction.service;

import com.situ.jurisdiction.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ZZQ
* @description 针对表【sys_role_menu】的数据库操作Service
* @createDate 2023-09-15 16:17:03
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void setMenusByRoleId(Long[] menuIds, Long roleId);
}
