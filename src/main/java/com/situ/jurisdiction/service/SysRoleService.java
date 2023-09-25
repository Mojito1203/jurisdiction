package com.situ.jurisdiction.service;

import com.situ.jurisdiction.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author ZZQ
* @description 针对表【sys_role】的数据库操作Service
* @createDate 2023-09-15 16:17:03
*/
public interface SysRoleService extends IService<SysRole> {

    List<Long> findRolesByUserId(Long userId);

    void del(Long id) throws Exception;
    boolean insert(SysRole sysRole) throws Exception;
    //修改角色
    boolean update(SysRole sysRole) throws Exception;

    List<Map<String, Integer>> getRoleMenuCount();
}
