package com.situ.jurisdiction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.situ.jurisdiction.entity.SysRole;
import com.situ.jurisdiction.util.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController{
    @Value("${stedu.pageSize}")
    private Integer pageSize;
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public R findAll(Integer pageNum, String name, String code) {
        PageHelper.startPage(pageNum,pageSize);
        //new QueryWrapper<SysRole>().like("name", name)

        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>()
                .like("name", name)
                .like("code", code);
        List<SysRole> sysRoles = sysRoleService.list(wrapper);
        PageInfo<SysRole> pageInfo = new PageInfo<>(sysRoles);
        return R.ok("查询成功", pageInfo);
    }

    @PutMapping("/menu/{roleId}")
    public R setMenusByRoleId(@RequestBody Long[] menuIds, @PathVariable("roleId") Long roleId) {
        sysRoleMenuService.setMenusByRoleId(menuIds, roleId);
        return R.ok("分配菜单成功");
    }

    @GetMapping
    public R findAll() {
        List<SysRole> sysRoles = sysRoleService.list();
        return R.ok(sysRoles);
    }

    @PostMapping
    public R add(@Validated @RequestBody SysRole sysRole) {
        try {
            sysRoleService.insert(sysRole);
            return R.ok("添加成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @PutMapping
    public R update(@RequestBody SysRole sysRole) {
        try {
            sysRoleService.update(sysRole);
            return R.ok("修改成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable("id") Long id) {
        try {
            sysRoleService.del(id);
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return R.ok(sysRole);
    }

    @GetMapping("getUserMenu")
    public R getRoleMenu(){
        return R.ok(sysRoleService.getRoleMenuCount());
    }


}