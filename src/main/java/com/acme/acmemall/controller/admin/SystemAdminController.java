package com.acme.acmemall.controller.admin;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.ApiBase;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.RoleVo;
import com.acme.acmemall.service.ISysRoleService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.PageUtils;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/12 16:59
 */
@RestController
@RequestMapping("/api/system/admin")
public class SystemAdminController extends ApiBase {

    @Resource
    ISysRoleService sysRoleService;
    @Resource
    IUserService userService;

    @RequestMapping("/get-admin")
    public Object getSystemAdmin(@LoginUser LoginUserVo loginUser) {
        return toResponsSuccess(loginUser);
    }

    @RequestMapping("/get-role")
    public Object getSystemRole(@LoginUser LoginUserVo loginUser,
                                @RequestParam(value = "roleName", defaultValue = "") String roleName,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        Map params = Maps.newHashMap();
        if (StringUtils.isNotEmpty(roleName)) {
            params.put("roleName", roleName);
        }
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "create_time");
        params.put("order", "desc");
        PageHelper.startPage(page, size, true);
        List<RoleVo> roles = sysRoleService.getAllRoles(params);
        PageInfo pageInfo = new PageInfo(roles);
        PageUtils pager = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, pager);
    }

    @PostMapping("/add-role")
    public Object addRole(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        JSONObject reqObj = getJsonRequest();
        if (reqObj == null) {
            return toResponsFail("参数错误");
        }
        RoleVo roleVo = reqObj.toJavaObject(RoleVo.class);
        return sysRoleService.addRole(roleVo);
    }

    @PostMapping("/update-role")
    public Object updateRole(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        JSONObject reqObj = getJsonRequest();
        if (reqObj == null) {
            return toResponsFail("参数错误");
        }
        RoleVo roleVo = reqObj.toJavaObject(RoleVo.class);
        return sysRoleService.updateRole(roleVo);
    }

    @PostMapping("/delete-role")
    public Object deleteRole(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        JSONObject reqObj = getJsonRequest();
        if (reqObj == null) {
            return toResponsFail("参数错误");
        }
        Long roleId = reqObj.getLong("roleId");
        if (roleId == null) {
            return toResponsFail("参数错误");
        }
        return sysRoleService.deleteRole(roleId);
    }
}
