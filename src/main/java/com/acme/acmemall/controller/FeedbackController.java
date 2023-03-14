package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.FeedbackVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IFeedBackService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @description: 反馈
 * @author: ihpangzi
 * @time: 2023/3/14 9:14
 */
@Api(tags = "反馈")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController extends ApiBase {

    @Autowired
    IFeedBackService feedbackService;

    /**
     * 添加反馈
     */
    @ApiOperation(value = "添加反馈")
    @PostMapping("save")
    public Object save(@LoginUser LoginUserVo loginUser) {
        JSONObject feedbackJson = super.getJsonRequest();
        if (null != feedbackJson) {
            FeedbackVo feedbackVo = FeedbackVo.builder().addTime(new Date()).build();
            feedbackVo.feedBack(loginUser, feedbackJson);
            feedbackService.feedBack(feedbackVo);
            return ResultMap.ok("感谢你的反馈," + feedbackVo.getMsgId());
        }
        return ResultMap.error("反馈失败");
    }
}
