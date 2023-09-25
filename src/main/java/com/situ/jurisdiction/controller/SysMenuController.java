package com.situ.jurisdiction.controller;

import com.situ.jurisdiction.dto.SysMenuDto;
import com.situ.jurisdiction.entity.SysMenu;
import com.situ.jurisdiction.entity.SysUser;
import com.situ.jurisdiction.service.SysMenuService;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    //根据角色Id查询已经分配的菜单的id
    @GetMapping("/menu/{roleId}")
    public R getMenuIdsByRoleId(@PathVariable("roleId") Long roleId) {
        List<Long> menuIds = sysMenuService.getMenuIdsByRoleId(roleId);
        return R.ok(menuIds);
    }


    @GetMapping("/nav")
    public R nav(Principal principal) {
        //获取当前用户登录用户的用户名
        String username = principal.getName();
        //获取当前用户信息
        SysUser sysUser = sysUserService.getSysUser(username);
        //获取用户ID
        Long userId = sysUser.getId();

        //获取当前用户的角色权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(userId);
        //将权限信息转换为字符串数组
        String[] authorityInfoArr = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        //获取当前用户菜单信息 -- 树形菜单
        List<SysMenuDto> menuNav = sysMenuService.getMenuNav(userId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("authorityInfo", authorityInfoArr);
        map.put("nav", menuNav);
        return R.ok(map);
    }

    @GetMapping
    public R treeMenu(){
        return R.ok(sysMenuService.getAllMenu());
    }

    @PostMapping
    public R add(@RequestBody SysMenu sysMenu) {
        try {
            sysMenuService.add(sysMenu);
            return R.ok("添加成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping
    public R update(@RequestBody SysMenu sysMenu) {
        try {
            sysMenuService.make(sysMenu);
            return R.ok("修改成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return R.ok(sysMenu);
    }

    @PostMapping("/{id}")
    public R del(@PathVariable("id") Long id) {
        try {
            sysMenuService.del(id);
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}