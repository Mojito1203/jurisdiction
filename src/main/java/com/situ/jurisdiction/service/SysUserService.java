package com.situ.jurisdiction.service;

import com.situ.jurisdiction.entity.SysMenu;
import com.situ.jurisdiction.entity.SysRole;
import com.situ.jurisdiction.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ZZQ
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2023-09-15 16:17:03
*/
public interface SysUserService extends IService<SysUser> {
    SysUser getSysUser(String username);

    String getUserAuthorityInfo(Long userId);

    List<SysRole> getAllSysRoleByUserId(Long userId);

    List<SysMenu> getAllSysMenuByUserId(Long userId);

    void clearUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);


}
