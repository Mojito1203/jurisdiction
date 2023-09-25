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

    //void add(SysUser sysUser, Long[] roleIds) throws Exception;
    void del(Long id) throws Exception;
    //void update(SysUser sysUser, Long[] roleIds) throws Exception;
    //添加用户
    boolean insert(SysUser sysUser) throws Exception;
    //修改用户
    boolean make(SysUser sysUser) throws Exception;

    boolean updatePwd(Long id, String oldPwd, String newPwd, String newAgainPwd) throws Exception;

    List<SysUser> findAll();

    SysUser findById(Long id);
    List<Long> findRolesByUserId(Long userId);


}
