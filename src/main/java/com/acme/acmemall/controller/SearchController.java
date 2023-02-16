package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.QueryParam;
import com.acme.acmemall.model.KeywordsVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.SearchHistoryVo;
import com.acme.acmemall.service.IKeywordsService;
import com.acme.acmemall.service.ISearchHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:商品搜索
 * @author: ihpangzi
 * @time: 2023/2/16 9:28
 */
@Api(tags = "商品搜索")
@RestController
@RequestMapping("/api/search")
public class SearchController extends ApiBase {

    @Autowired
    IKeywordsService keywordsService;

    @Autowired
    ISearchHistoryService searchHistoryService;

    /**
     * index
     */
    @ApiOperation(value = "搜索商品列表")
    @GetMapping("index")
    public Object index(@LoginUser LoginUserVo loginUser) {
        // 默认关键词获取
        Map<String, Object> resultObj = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("is_default", 1);
        param.put("page", 1);
        param.put("limit", 1);
        param.put("sidx", "id");
        param.put("order", "asc");
        List<KeywordsVo> keywords = keywordsService.queryKeywordsList(param);
        //取出输入框默认的关键词
        KeywordsVo defaultKeyword = null != keywords && keywords.size() > 0 ? keywords.get(0) : null;
        //取出热闹关键词
        param = new HashMap<>();
        param.put("fields", "distinct keyword,is_hot");
        param.put("page", 1);
        param.put("limit", 10);
        param.put("sidx", "id");
        param.put("order", "asc");
        QueryParam query = new QueryParam(param);
        List<Map> hotKeywordList = keywordsService.hotKeywordList(query);
        //
        param = new HashMap<>();
        param.put("user_id", loginUser.getUserId());
        param.put("fields", "distinct keyword");
        param.put("page", 1);
        param.put("limit", 10);
//        param.put("sidx", "id");
//        param.put("order", "asc");
        List<SearchHistoryVo> historyVoList = searchHistoryService.querySearchHistoryList(param);
        String[] historyKeywordList = new String[historyVoList.size()];
        if (null != historyVoList) {
            int i = 0;
            for (SearchHistoryVo historyVo : historyVoList) {
                historyKeywordList[i] = historyVo.getKeyword();
                i++;
            }
        }
        //
        resultObj.put("defaultKeyword", defaultKeyword);
        resultObj.put("historyKeywordList", historyKeywordList);
        resultObj.put("hotKeywordList", hotKeywordList);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　helper
     */
    @ApiOperation(value = "搜索商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping("helper")
    public Object helper(@LoginUser LoginUserVo loginUser, String keyword) {
        Map param = new HashMap();
        param.put("fields", "distinct keyword");
        param.put("keyword", keyword);
        param.put("limit", 10);
        param.put("offset", 0);
        List<KeywordsVo> keywords = keywordsService.queryKeywordsList(param);
        String[] keys = new String[keywords.size()];
        if (null != keywords) {
            int i = 0;
            for (KeywordsVo keywordsVo : keywords) {
                keys[i] = keywordsVo.getKeyword();
                i++;
            }
        }
        return toResponsSuccess(keys);
    }

    /**
     * 　清空历史搜索记录
     */
    @PostMapping("clearhistory")
    public Object clearhistory(@LoginUser LoginUserVo loginUser) {
        searchHistoryService.deleteByUserId(loginUser.getUserId());
        //
        return toResponsSuccess("");
    }
}
