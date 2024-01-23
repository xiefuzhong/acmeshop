package com.acme.acmemall.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:物流信息
 * @author: ihpangzi
 * @time: 2024/1/23 13:55
 */
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController extends ApiBase {

    @PostMapping("/update")
    public String updateLogistics() {

        return "success";
    }
}
