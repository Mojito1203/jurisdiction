package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.dto.SysMenuDto;
import com.situ.jurisdiction.entity.SysMenu;
import com.situ.jurisdiction.entity.SysRoleMenu;
import com.situ.jurisdiction.mapper.SysMenuMapper;
import com.situ.jurisdiction.mapper.SysRoleMenuMapper;
import com.situ.jurisdiction.mapper.SysUserMapper;
import com.situ.jurisdiction.service.SysMenuService;
import com.situ.jurisdiction.service.SysUserService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZQ
 * @description 针对表【sys_menu】的数据库操作Service实现
 * @createDate 2023-09-15 16:17:02
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //根据用户id查询菜单
    @Override
    public List<SysMenuDto> getMenuNav(Long userId) {
        List<SysMenu> allSysMenuByUserId = sysUserService.getAllSysMenuByUserId(userId);
        List<SysMenu> sysMenus = buildMenuTree(allSysMenuByUserId);
        return getSysMenuDto(sysMenus);
    }

    //查询所有菜单
    @Override
    public List<SysMenu> getAllMenu() {
        List<SysMenu> orderNumList = this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));
        List<SysMenu> sysMenus = buildMenuTree(orderNumList);
        return sysMenus;
    }

    @Override
    public void add(SysMenu sysMenu) throws Exception {
        Long parentId = sysMenu.getParentId();
        Integer type = sysMenu.getType();
        if (parentId == 0 && type != 0) {
            throw new Exception("类型应为目录");
        }

        if (parentId != 0) {
            SysMenu menu = this.getById(parentId);
            if (menu.getParentId() == 0 && type != 1) {
                throw new Exception("类型应为菜单");
            }
            if (menu.getParentId() != 0 && type != 2) {
                throw new Exception("类型应为按钮");
            }
        }

        List<SysMenu> sysMenuList = this.list(new QueryWrapper<SysMenu>().eq("name", sysMenu.getName()));
        if (sysMenuList.size() > 0) {
            throw new Exception("该菜单已存在");
        }
        List<SysMenu> list = this.list(new QueryWrapper<SysMenu>().eq("parent_id", sysMenu.getParentId()));
        Integer num = list.stream().map(SysMenu::getOrderNum).max(Integer::compareTo).orElse(0);
        sysMenu.setOrderNum(num + 1);
        sysMenu.setStatus(1);
        sysMenu.setCreated(new Date());
        sysMenu.setUpdated(new Date());
        if (!this.save(sysMenu)) {
            throw new Exception("添加失败");
        }
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
    }

    @Override
    public void make(SysMenu sysMenu) throws Exception{
        SysMenu m = this.getById(sysMenu.getId());
        long count = this.list(new QueryWrapper<SysMenu>().eq("parent_id", sysMenu.getId())).stream().count();
        if(count > 0 && sysMenu.getParentId() != m.getParentId()) {
            throw new Exception("该菜单下还有子菜单,无法修改");
        }
        Long parentId = sysMenu.getParentId();
        Integer type = sysMenu.getType();
        if(parentId == 0 && type != 0) {
            throw new Exception("类型应为目录");
        }

        if(parentId != 0) {
            SysMenu menu = this.getById(parentId);
            if (menu.getParentId() == 0 && type != 1) {
                throw new Exception("类型应为菜单");
            }
            if(menu.getParentId() != 0 && type != 2) {
                throw new Exception("类型应为按钮");
            }
        }
        List<SysMenu> sysMenuList = this.list(new QueryWrapper<SysMenu>().eq("name", sysMenu.getName()));
        if(sysMenuList.size() > 0 && !sysMenu.getName().equals(m.getName())) {
            throw new Exception("修改后的菜单名称，不能与已有的重复");
        }

        if (!this.updateById(sysMenu)) {
            throw new Exception("修改失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(Long id) throws Exception {
        if(this.getById(id) == null) {
            throw new Exception("无此菜单, 无法删除");
        }
        long count = this.list(new QueryWrapper<SysMenu>().eq("parent_id", id)).stream().count();
        if(count > 0) {
            throw new Exception("该菜单下还有子菜单,无法删除");
        }
        if(!this.removeById(id)) {
            throw new Exception("删除菜单失败");
        }
        sysUserService.clearUserAuthorityInfoByMenuId(id);
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
        //sysUserService.clearUserAuthorityInfoByMenuId(id);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        List<Long> menuIds = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        return menuIds;
    }


    //构建树形结构方法
    public List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        menuList.stream().forEach(sysMenu -> {
            List<SysMenu> collect = menuList.stream().filter(item -> item.getParentId() == sysMenu.getId()).collect(Collectors.toList());
            if (collect.size() != 0) {
                sysMenu.setChildren(collect);
            }
        });
        List<SysMenu> treeMenuList = menuList.stream().filter(res -> res.getParentId() == 0).collect(Collectors.toList());
        return treeMenuList;
    }


    //将SysMenu转换为SysMenuDto
    public List<SysMenuDto> getSysMenuDto(List<SysMenu> sysMenus) {
        List<SysMenuDto> sysMenuDtos = new ArrayList<>();
        sysMenus.stream().forEach(sysMenu -> {
            SysMenuDto sysMenuDto = new SysMenuDto();
            sysMenuDto.setId(sysMenu.getId());
            sysMenuDto.setName(sysMenu.getPerms());
            sysMenuDto.setTitle(sysMenu.getName());
            sysMenuDto.setComponent(sysMenu.getComponent());
            sysMenuDto.setIcon(sysMenu.getIcon());
            sysMenuDto.setPath(sysMenu.getPath());

            if (sysMenu.getChildren() != null && sysMenu.getChildren().size() > 0) {
                sysMenuDto.setChildren(getSysMenuDto(sysMenu.getChildren()));
            }
            sysMenuDtos.add(sysMenuDto);
        });
        return sysMenuDtos;
    }
}




