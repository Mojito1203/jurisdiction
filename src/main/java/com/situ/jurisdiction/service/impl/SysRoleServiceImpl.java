package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.situ.jurisdiction.entity.SysMenu;
import com.situ.jurisdiction.entity.SysRole;
import com.situ.jurisdiction.entity.SysRoleMenu;
import com.situ.jurisdiction.entity.SysUserRole;
import com.situ.jurisdiction.mapper.SysMenuMapper;
import com.situ.jurisdiction.mapper.SysRoleMapper;
import com.situ.jurisdiction.mapper.SysRoleMenuMapper;
import com.situ.jurisdiction.mapper.SysUserRoleMapper;
import com.situ.jurisdiction.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author ZZQ
* @description 针对表【sys_role】的数据库操作Service实现
* @createDate 2023-09-15 16:17:03
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper ;

    @Override
    public List<Long> findRolesByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        return roleIds;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(Long id) throws Exception {
        if(sysRoleMapper.selectById(id) == null) {
            throw new Exception("用户id不存在");
        }

        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        sysUserRoleMapper.delete(wrapper);

        QueryWrapper<SysRoleMenu> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("role_id", id);
        sysRoleMenuMapper.delete(wrapper2);
        //if (userRoleMapper.delete(wrapper) != userRoleMapper.selectCount(wrapper)) {
        //    throw new Exception("删除用户角色失败");
        //}

        if(sysRoleMapper.deleteById(id) != 1) {
            throw new Exception("删除失败");
        }
    }

    @Override
    public boolean insert(SysRole sysRole) throws Exception {
        String name = sysRole.getName();
        if (this.list(new QueryWrapper<SysRole>().eq("name", name)).size() > 0) {
            throw new Exception("该角色名称已存在，不能重复添加");
        }
        return this.save(sysRole);
    }

    @Override
    public boolean update(SysRole sysRole) throws Exception {
        SysRole s = sysRoleMapper.selectById(sysRole.getId());
        if(s == null) {
            throw new Exception("该角色不存在，无法修改");
        }

        String name = sysRole.getName();
        List<SysRole> sysRoleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().eq("name", name));
        if (sysRoleList.size() > 0 && !sysRole.getName().equals(s.getName())) {
            throw new Exception("该角色名称已存在，不能重复添加");
        }
        return this.updateById(sysRole);
    }

    @Override
    public List<Map<String, Integer>> getRoleMenuCount() {
        return sysRoleMapper.selectRoleMenuCount();
    }
}




