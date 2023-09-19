package com.situ.jurisdiction.controller;

import com.situ.jurisdiction.dto.SysMenuDto;
import com.situ.jurisdiction.entity.SysUser;
import com.situ.jurisdiction.service.SysMenuService;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}