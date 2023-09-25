package com.situ.jurisdiction.mapper;

import com.situ.jurisdiction.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author ZZQ
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2023-09-15 16:17:03
* @Entity com.situ.jurisdiction.entity.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser findById(Long id);

    List<SysUser> findAll();
}




