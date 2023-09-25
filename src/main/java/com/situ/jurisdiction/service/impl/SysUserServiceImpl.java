package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.entity.*;
import com.situ.jurisdiction.mapper.*;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SysUserMapper sysUserMapper;

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
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().in("role_id", roleId));
        userRoleList.stream().forEach(userRoleStream -> clearUserAuthorityInfo(userRoleStream.getUserId()));
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        //获取菜单对应的角色
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("menu_id", menuId));
        List<Long> roleIdList = sysRoleMenus.stream().map(sysRoleMenu -> sysRoleMenu.getRoleId()).collect(Collectors.toList());

        //获取角色对应的用户id
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().in(roleIdList.size()>0,"role_id", roleIdList));

        sysUserRoles.stream().forEach(sysUserRole -> clearUserAuthorityInfo(sysUserRole.getUserId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(Long id) throws Exception {
        if(sysUserMapper.selectById(id) == null) {
            throw new Exception("改用户不存在");
        }
        if(sysUserMapper.deleteById(id) != 1) {
            throw new Exception("删除用户失败");
        }
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        sysUserRoleMapper.delete(wrapper);
    }

    @Override
    public boolean insert(SysUser sysUser) throws Exception {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", sysUser.getUsername());
        List<SysUser> sysUserList = sysUserMapper.selectList(wrapper);
        if(sysUserList.size() > 0) {
            throw new Exception("该用户名已存在");
        }
        return this.save(sysUser);
    }

    @Override
    public boolean make(SysUser sysUser) throws Exception {
        SysUser s = sysUserMapper.selectById(sysUser.getId());
        if(s == null) {
            throw new Exception("该用户不存在，无法修改");
        }

        List<SysUser> sysUserList = sysUserMapper.selectList(new QueryWrapper<SysUser>().eq("username", sysUser.getUsername()));
        if(sysUserList.size() > 0 && !sysUser.getUsername().equals(s.getUsername())) {
            throw new Exception("修改后的用户名已存在，无法修改");
        }
        return this.updateById(sysUser);
    }

    @Override
    public boolean updatePwd(Long id, String oldPwd, String newPwd, String newAgainPwd) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = null;
        SysUser sysUser = sysUserMapper.findById(id);
        pwd = sysUser.getPassword();
        if(oldPwd.equals(newPwd)) {
            throw new Exception("新旧密码不能相同");
        }
        if(!newAgainPwd.equals(newPwd)) {
            throw new Exception("您输入的两次密码不一致");
        }
        //oldPwd = encoder.encode(oldPwd);
        if(!encoder.matches(oldPwd, pwd)) {
            throw new Exception("您输入的旧密码不正确");
        }
        newPwd = encoder.encode(newPwd);
        sysUser.setPassword(newPwd);
        return sysUserMapper.updateById(sysUser) == 1;
    }

    @Override
    public List<SysUser> findAll() {
        return sysUserMapper.findAll();
    }

    @Override
    public SysUser findById(Long id) {
        return sysUserMapper.findById(id);
    }

    @Override
    public List<Long> findRolesByUserId(Long userId) {
        List<Long> roles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        return roles;
    }
}




