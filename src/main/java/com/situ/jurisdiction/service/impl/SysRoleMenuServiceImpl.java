package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.entity.SysRoleMenu;
import com.situ.jurisdiction.mapper.SysRoleMenuMapper;
import com.situ.jurisdiction.service.SysRoleMenuService;
import com.situ.jurisdiction.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author ZZQ
* @description 针对表【sys_role_menu】的数据库操作Service实现
* @createDate 2023-09-15 16:17:03
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService{

    @Autowired
    private SysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setMenusByRoleId(Long[] menuIds, Long roleId) {
        //删除roleId所有的角色菜单关系
        this.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        //重新添加
        //Long[] menuIds, Long roleId --> ArrayList<SysRoleMenu>
        List<SysRoleMenu> sysRoleMenuList = Arrays.stream(menuIds).map(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(roleId);
            return sysRoleMenu;
        }).collect(Collectors.toList());
        this.saveBatch(sysRoleMenuList);

        //清空缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);
    }
}




