package com.acme.acmemall.controller.admin;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.ApiBase;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.MembersVo;
import com.acme.acmemall.model.RoleVo;
import com.acme.acmemall.service.ISysRoleService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.PageUtils;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @PostMapping("/add-member")
    public Object addMember(@LoginUser LoginUserVo loginUser) {
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
        MembersVo membersVo = reqObj.toJavaObject(MembersVo.class);
        if (membersVo == null) {
            return toResponsFail("参数错误");
        }
        String pwd = DigestUtils.sha256Hex(reqObj.getString("password"));
        String comfirmPwd = DigestUtils.sha256Hex(reqObj.getString("confirmPassword"));
        if (!Objects.equals(pwd, comfirmPwd)) {
            return toResponsFail("两次密码输入不一致");
        }
        membersVo.addMember(loginUser.getUserId(), pwd, reqObj.getInteger("status"));
        return userService.addMember(membersVo);
    }

    @RequestMapping("/get-admin")
    public Object getSystemAdmin(@LoginUser LoginUserVo loginUser,
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
        params.put("order", "desc");
        PageHelper.startPage(page, size, true);
        List<MembersVo> members = userService.getAdminUsers(params);
        PageInfo pageInfo = new PageInfo(members);
        PageUtils pager = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, pager);
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
        if (roleVo == null) {
            return toResponsFail("参数错误");
        }
        roleVo.addRole(loginUser.getUserId());
        logger.info("addRole: " + roleVo);
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
        roleVo.updateRole(loginUser.getUserId());
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

    @GetMapping("/get")
    public Object get(@LoginUser LoginUserVo loginUser, @RequestParam(value = "roleId", defaultValue = "0") Long roleId) {
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        if (roleId == 0) {
            return toResponsFail("参数错误");
        }
        RoleVo roleVo = sysRoleService.getRoleById(roleId);
        if (roleVo == null) {
            return toResponsFail("角色不存在");
        }
        return toResponsSuccess(roleVo);
    }

}
