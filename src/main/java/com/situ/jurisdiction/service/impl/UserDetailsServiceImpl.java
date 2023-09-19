package com.situ.jurisdiction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.situ.jurisdiction.entity.CustomUser;
import com.situ.jurisdiction.entity.SysUser;
import com.situ.jurisdiction.mapper.SysUserMapper;
import com.situ.jurisdiction.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getSysUser(username);
        if(sysUser==null){
            throw new UsernameNotFoundException("该用户不存在");
        }
        return new CustomUser(sysUser,new TreeSet<>());
    }
}
