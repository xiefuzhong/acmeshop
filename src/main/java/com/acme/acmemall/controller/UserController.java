package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.PageUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        logger.info(userGoods);
        JSONObject request = getJsonRequest();
        logger.info("request:" + request.toString());
        userGoods.bindQueryParam(loginUser.getUserId(), request.getInteger("goodsId"));
        UserGoods shareGoods = userService.queryShareGoods(userGoods);
        if (shareGoods == null) {
            userGoods = UserGoods.builder().userId(loginUser.getUserId()).build();
            userGoods.add(request);
            userService.saveShareGoods(userGoods);
        }
        return ResultMap.response(ResultCodeEnum.SUCCESS, userGoods);
    }

    @ApiOperation(value = "删除分享记录")
    @RequestMapping("delShareGoods")
    public Object delShareGoods(@LoginUser LoginUserVo loginUser, UserGoods userGoods) {
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

    @RequestMapping("/check-admin")
    public Object checkAdmin(@LoginUser LoginUserVo loginUser) {
        Boolean flag = userService.checkAdmin(loginUser.getUserId());
        Map resultMap = Maps.newHashMap();
        resultMap.put("checkFlag", flag ? "Y" : "N");
        return toResponsSuccess(resultMap);
    }

    @GetMapping("/list-mer")
    public Object findByMerchantId(@LoginUser LoginUserVo loginUser,
                                   @RequestParam("merchant_id") Integer merchant_id,
                                   @RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size) {
        if (loginUser == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        LoginUserVo userVo = userService.queryByUserId(loginUser.getUserId());
        if (userVo == null || userVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        if (userVo.getMerchantId().intValue() != merchant_id.intValue()) {
            return ResultMap.badArgumentValue("merchant_id参数值不对");
        }
        Map paramMap = Maps.newHashMap();
        paramMap.put("merchantId", userVo.getMerchantId());
        Map params = Maps.newHashMap();
        params.put("merchant_id", merchant_id);
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");

        //查询列表数据
        PageHelper.startPage(page, size);
        List<LoginUserVo> userVoList = userService.queryUserList(params);

        PageInfo pageInfo = new PageInfo<>(userVoList);
        PageUtils pageData = new PageUtils(pageInfo);
        return toResponsSuccess(pageData);
    }

}
