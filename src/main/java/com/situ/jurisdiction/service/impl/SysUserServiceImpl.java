package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.entity.*;
import com.situ.jurisdiction.mapper.*;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZQ
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2023-09-15 16:17:03
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUser getSysUser(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    //根据用户ID获取用户权限字符串（角色 + 权限）
    @Override
    public String getUserAuthorityInfo(Long userId) {
        String collect = "";
        String authorityInfoCache = (String) redisUtil.get("GrantedAuthority:" + userId);
        if (authorityInfoCache != null && !("").equals(authorityInfoCache)) {
            collect = authorityInfoCache;
            redisUtil.expire("GrantedAuthority:" + userId, 60 * 60);
        } else {
            //根据用户ID获取用户所有的角色
            List<SysRole> allSysRoleByUserId = getAllSysRoleByUserId(userId);
            if (allSysRoleByUserId.size() > 0) {
                collect = allSysRoleByUserId.stream().map(sysRole -> "ROLE_" + sysRole.getCode()).collect(Collectors.joining(","));
                collect += ",";
            }
            //根据用户ID获取用户所有的菜单
            List<SysMenu> allSysMenuByUserId = getAllSysMenuByUserId(userId);
            if (allSysMenuByUserId.size() > 0) {
                String perms = allSysMenuByUserId.stream().map(menu -> menu.getPerms()).collect(Collectors.joining(","));
                collect = collect.concat(perms);
            }
            redisUtil.set("GrantedAuthority:" + userId, collect, 60 * 60);
        }
        return collect;
    }

    @Override
    public List<SysRole> getAllSysRoleByUserId(Long userId) {
        List<SysRole> allSysRoleByUserId = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id", "SELECT `role_id` FROM `sys_user_role` WHERE `user_id`=" + userId));
        return allSysRoleByUserId;
    }

    @Override
    public List<SysMenu> getAllSysMenuByUserId(Long userId) {
        List<SysMenu> allSysMenuByUserId = sysMenuMapper.selectList(new QueryWrapper<SysMenu>()
                .inSql("id", "SELECT DISTINCT rm.menu_id FROM `sys_user_role` ur, `sys_role_menu` rm WHERE ur.role_id=rm.role_id AND ur.user_id=" + userId)
        );
        return allSysMenuByUserId;
    }

    @Override
    public void clearUserAuthorityInfo(Long userId) {
        redisUtil.del("GrantedAuthority:" + userId);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().in("roleId", roleId));
        userRoleList.stream().forEach(userRoleStream -> clearUserAuthorityInfo(userRoleStream.getUserId()));
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        //获取菜单对应的角色
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("menu_id", menuId));
        List<Long> roleIdList = sysRoleMenus.stream().map(sysRoleMenu -> sysRoleMenu.getRoleId()).collect(Collectors.toList());

        //获取角色对应的用户id
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().in("role_id", roleIdList));

        sysUserRoles.stream().forEach(sysUserRole -> clearUserAuthorityInfo(sysUserRole.getUserId()));
    }
}




