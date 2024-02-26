package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/26 16:52
 */
@RestController
@RequestMapping("/api/wechat")
public class WeChatController {
    @GetMapping("/get-mobile")
    public Object list(@LoginUser LoginUserVo loginUser) {
        Map<String, Object> param = Maps.newHashMap();
        return ResultMap.ok();
    }
}
