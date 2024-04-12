package com.acme.acmemall.controller.admin;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.controller.ApiBase;
import com.acme.acmemall.model.LoginUserVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/12 16:59
 */
@RestController
@RequestMapping("/api/system/admin")
public class SystemAdminController extends ApiBase {

    @RequestMapping("/get-admin")
    public Object getSystemAdmin(@LoginUser LoginUserVo loginUser) {
        return toResponsSuccess(loginUser);
    }

    @RequestMapping("/get-role")
    public Object getSystemRole() {
        return toResponsSuccess("admin");
    }
}
