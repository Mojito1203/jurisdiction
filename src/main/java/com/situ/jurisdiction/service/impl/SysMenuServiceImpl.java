package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.dto.SysMenuDto;
import com.situ.jurisdiction.entity.SysMenu;
import com.situ.jurisdiction.mapper.SysMenuMapper;
import com.situ.jurisdiction.mapper.SysUserMapper;
import com.situ.jurisdiction.service.SysMenuService;
import com.situ.jurisdiction.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //根据用户id查询菜单
    @Override
    public List<SysMenuDto> getMenuNav(Long userId) {
        List<SysMenu> allSysMenuByUserId = sysUserService.getAllSysMenuByUserId(userId);
        List<SysMenu> sysMenus = buildMenuTree(allSysMenuByUserId);
        return getSysMenuDto(sysMenus);
    }

    //查询所有菜单
    @Override
    public List<SysMenu> getAllMenu(){
        List<SysMenu> orderNumList = this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));
        List<SysMenu> sysMenus = buildMenuTree(orderNumList);
        return sysMenus;
    }

    //构建树形结构方法
    public List<SysMenu> buildMenuTree(List<SysMenu> menuList){
        menuList.stream().forEach(sysMenu -> {
            List<SysMenu> collect = menuList.stream().filter(item -> item.getParentId() == sysMenu.getId()).collect(Collectors.toList());
            if(collect.size() !=0){
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




