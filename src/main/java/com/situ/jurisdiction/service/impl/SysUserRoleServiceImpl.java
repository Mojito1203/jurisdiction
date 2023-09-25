package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.entity.SysUserRole;
import com.situ.jurisdiction.mapper.SysUserRoleMapper;
import com.situ.jurisdiction.service.SysUserRoleService;
import com.situ.jurisdiction.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author ZZQ
* @description 针对表【sys_user_role】的数据库操作Service实现
* @createDate 2023-09-15 16:17:02
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService{

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setRolesByUserId(Long[] roleIds, Long userId) {
        //删除roleId所有的角色菜单关系
        this.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //重新添加
        //Long[] menuIds, Long roleId --> ArrayList<SysRoleMenu>
        List<SysUserRole> sysUserRoleList = Arrays.stream(roleIds).map(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            return sysUserRole;
        }).collect(Collectors.toList());
        this.saveBatch(sysUserRoleList);

        //清空缓存
        sysUserService.clearUserAuthorityInfo(userId);
    }
}




