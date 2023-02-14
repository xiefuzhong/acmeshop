package com.acme.acmemall.service;

import com.acme.acmemall.model.GoodsIssueVo;

import java.util.List;
import java.util.Map;

public interface IGoodsIssueService {

    GoodsIssueVo queryObject(long id);

    List<GoodsIssueVo> queryIssueList(Map<String,Object> map);
}
