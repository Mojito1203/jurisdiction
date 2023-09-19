package com.situ.jurisdiction.service;

import com.situ.jurisdiction.dto.SysMenuDto;
import com.situ.jurisdiction.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ZZQ
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2023-09-15 16:17:02
*/
public interface SysMenuService extends IService<SysMenu> {

    //根据用户ID获取菜单信息
    List<SysMenuDto> getMenuNav(Long userId);

    //查询所有菜单
    List<SysMenu> getAllMenu();

}
