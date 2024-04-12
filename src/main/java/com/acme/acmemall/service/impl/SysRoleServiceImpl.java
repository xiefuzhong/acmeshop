package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.dao.SysRoleMapper;
import com.acme.acmemall.model.RoleVo;
import com.acme.acmemall.service.ISysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/12 17:40
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    SysRoleMapper roleMapper;

    /**
     * @return
     */
    @Override
    public List<RoleVo> getAllRoles(Map<String, Object> map) {
        return roleMapper.queryList(map);
    }

    /**
     * @param role
     * @return
     */
    @Override
    public ResultMap addRole(RoleVo role) {
        return roleMapper.save(role) > 0 ? ResultMap.ok() : ResultMap.error();
    }

    /**
     * @param roleVo
     * @return
     */
    @Override
    public ResultMap updateRole(RoleVo roleVo) {
        return roleMapper.update(roleVo) > 0 ? ResultMap.ok() : ResultMap.error();
    }

    /**
     * @param roleId
     * @return
     */
    @Override
    public ResultMap deleteRole(Long roleId) {
        return roleMapper.delete(roleId) > 0 ? ResultMap.ok() : ResultMap.error();
    }
}
