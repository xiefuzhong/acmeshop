package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.FootprintVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IFootprintService;
import com.acme.acmemall.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:足迹(浏览记录)
 * @author: ihpangzi
 * @time: 2023/3/18 13:35
 */

@Api(tags = "足迹")
@RestController
@RequestMapping("/api/footmark")
public class FootMarkController extends ApiBase {
    private final IFootprintService footprintService;

    @Autowired
    public FootMarkController(IFootprintService footprintService) {
        this.footprintService = footprintService;
    }

    /**
     * 清除足迹
     *
     * @param loginUser
     * @param footprintId
     * @return
     */
    @ApiOperation(value = "清除足迹")
    @ApiImplicitParams({@ApiImplicitParam(name = "footprintId", value = "足迹id", paramType = "path", required = true)})
    @GetMapping("delete")
    public Object delete(@LoginUser LoginUserVo loginUser, Integer footprintId) {
        logger.info("foot.del>>" + footprintId);
        if (footprintId == null) {
            return ResultMap.error("请求参数错误");
        }
        FootprintVo foot = footprintService.queryObject(footprintId);
        logger.info(foot);
        if (foot == null || !foot.operationCheck(loginUser)) {
            logger.info("foot.delete");
            return ResultMap.error("用户删除失败");
        }
        //删除当天的同一个商品的足迹
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", loginUser.getUserId());
        param.put("goodsId", foot.getGoods_id());
        footprintService.deleteByParam(param);

        return ResultMap.ok("删除成功");
    }


    /**
     * 获取足迹列表
     *
     * @param loginUser
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "获取足迹列表")
    @GetMapping("list")
    public Object list(@LoginUser LoginUserVo loginUser,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //查询列表数据
        PageHelper.startPage(page, size, true);
        List<FootprintVo> footprintVos = footprintService.queryListFootprint(loginUser.getUserId() + "");
        footprintVos.stream().forEach(foot -> foot.fmtAddTime());
        footprintVos.stream().sorted(Comparator.comparing(FootprintVo::getAdd_time).reversed()).collect(Collectors.toList());
        Map<String, List<FootprintVo>> footMap = footprintVos.stream().collect(Collectors.groupingBy(FootprintVo::getSort_add_time));
        List<List<FootprintVo>> footprintVoList = footMap.values().stream().collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(footprintVos);
        pageInfo.setList(footprintVoList);
        PageUtils pageUtil = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, pageUtil);
    }

}
