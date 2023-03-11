package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.acme.acmemall.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:商城客户
 * @author: ihpangzi
 * @time: 2023/3/10 17:15
 */
@Api(tags = "商城客户")
@RestController
@RequestMapping("/api/user")
public class UserController extends ApiBase {

    @Autowired
    IUserService userService;

    /**
     * 取当前用户分享历史
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "取当前用户分享历史")
    @RequestMapping("getShareGoods")
    public Object getShareGoods(@LoginUser LoginUserVo loginUser) {
        UserGoods userGoods = UserGoods.builder().userId(loginUser.getUserId()).build();
        List<UserGoods> userGoodsList = userService.queryShareList(userGoods);
        return ResultMap.response(ResultCodeEnum.SUCCESS, userGoodsList);
    }

    /**
     * 分享历史
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "分享历史")
    @RequestMapping("addShareGoods")
    public Object addShareGoods(@LoginUser LoginUserVo loginUser, UserGoods userGoods) {
        logger.info("add>>" + userGoods);
        userGoods.bindQueryParam(loginUser.getUserId(), userGoods.getGoodsId());
        UserGoods shareGoods = userService.queryShareGoods(userGoods);
        if (shareGoods == null) {
            userService.saveShareGoods(userGoods);
        }
        return ResultMap.response(ResultCodeEnum.SUCCESS, userGoods);
    }

    @ApiOperation(value = "删除分享记录")
    @RequestMapping("delShareGoods")
    public Object delShareGoods(@LoginUser LoginUserVo loginUser, UserGoods userGoods) {
        logger.info("delShareGoods>>" + userGoods);
        JSONObject request = getJsonRequest();
        userGoods.bindQueryParam(loginUser.getUserId(), request.getInteger("valueId"));
        UserGoods shareGoods = userService.queryShareGoods(userGoods);
        int result = 0;
        if (shareGoods != null) {
            logger.info(">>" + shareGoods);
            result = userService.delShareGoods(userGoods);
        }
        return result > 0 ? ResultMap.ok() : ResultMap.error();
    }

}
