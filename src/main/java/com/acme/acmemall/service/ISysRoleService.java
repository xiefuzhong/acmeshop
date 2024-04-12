package com.acme.acmemall.service;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.RoleVo;

import java.util.List;
import java.util.Map;

public interface ISysRoleService {

    List<RoleVo> getAllRoles(Map<String, Object> map);

    ResultMap addRole(RoleVo roleVo);

    ResultMap updateRole(RoleVo roleVo);

    ResultMap deleteRole(Long roleId);
}
