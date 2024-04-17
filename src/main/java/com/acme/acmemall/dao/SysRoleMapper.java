package com.acme.acmemall.dao;

import com.acme.acmemall.model.RoleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseDao<RoleVo> {

    int batchSaveUserRole(List<RoleVo> roles, Long userId);

    int deleteUserRoleByUserId(Long userId);
}
