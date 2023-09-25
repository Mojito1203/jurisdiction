package com.situ.jurisdiction.mapper;

import com.situ.jurisdiction.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author ZZQ
* @description 针对表【sys_role】的数据库操作Mapper
* @createDate 2023-09-15 16:17:03
* @Entity com.situ.jurisdiction.entity.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<Map<String, Integer>> selectRoleMenuCount();
}




