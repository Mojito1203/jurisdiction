package com.situ.jurisdiction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.situ.jurisdiction.entity.SysUser;
import com.situ.jurisdiction.util.R;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController{
    @Value("${stedu.pageSize}")
    private Integer pageSize;

    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/list/{pageNum}")
    public R findByPage(@PathVariable("pageNum") Integer pageNum, @RequestBody SysUser sysUser) {
        PageHelper.startPage(pageNum,pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>()
                .like("username", sysUser.getUsername())
                .like("city", sysUser.getCity());
        List<SysUser> sysUsers = sysUserService.list(wrapper);
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);
        return R.ok("查询成功", pageInfo);
    }

    @PutMapping("/role/{userId}")
    public R setRolesByUserId(@RequestBody Long[] roleIds, @PathVariable("userId") Long userId) {
        sysUserRoleService.setRolesByUserId(roleIds, userId);
        return R.ok("分配角色成功");
    }

    @GetMapping("/role/{userId}")
    public R findRolesByUserId(@PathVariable("userId") Long userId) {
        List<Long> roleIds = sysRoleService.findRolesByUserId(userId);
        return R.ok(roleIds);
    }


    @PostMapping("/updatePwd")
    public R updatePwd(Principal principal, String oldPwd, String newPwd, String newAgainPwd) {
        String username = principal.getName();
        SysUser sysUser = sysUserService.getSysUser(username);
        try {
            sysUserService.updatePwd(sysUser.getId(),oldPwd, newPwd, newAgainPwd);
            return R.ok("修改成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping
    public R add(@Validated @RequestBody SysUser sysUser) {
        sysUser.setPassword("123456");
        try {
            sysUserService.insert(sysUser);
            return R.ok("添加成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @PutMapping
    public R update(@RequestBody SysUser sysUser) {
        try {
            sysUserService.make(sysUser);
            return R.ok("修改成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable("id") Long id) {
        try {
            sysUserService.del(id);
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return R.ok(sysUser);
    }

    //重置密码
    @PostMapping("/resetPwd/{id}")
    public R resetPwd(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        BCryptPasswordEncoder pwd = new BCryptPasswordEncoder();
        String password = pwd.encode("123456");
        sysUser.setPassword(password);
        return sysUserService.updateById(sysUser) ? R.ok("重置成功") : R.error("重置失败");
    }

    @GetMapping("/logout")
    public R logout(Principal principal) {
        String name = principal.getName();
        SysUser sysUser = sysUserService.getSysUser(name);
        sysUserService.clearUserAuthorityInfo(sysUser.getId());
        return R.ok("退出成功");
    }



}