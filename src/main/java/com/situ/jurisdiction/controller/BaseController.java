package com.situ.jurisdiction.controller;


import com.situ.jurisdiction.service.*;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected SysUserService sysUserService;

    @Autowired
    protected SysMenuService sysMenuService;

    @Autowired
    protected SysRoleService sysRoleService;

    @Autowired
    protected SysUserRoleService sysUserRoleService;

    @Autowired
    protected SysRoleMenuService sysRoleMenuService;
}