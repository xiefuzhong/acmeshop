package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.CommentVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ICommentService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.PageUtils;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:评论
 * @author: ihpangzi
 * @time: 2023/3/17 14:27
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController extends ApiBase {

    @Autowired
    ICommentService commentService;

    @Resource
    IUserService userService;

    /**
     * 发表评论
     */
    @ApiOperation(value = "发表评论")
    @PostMapping("post")
    public Object postComment(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return ResultMap.error(400, "非有效用户操作");
        }

        JSONObject object = super.getJsonRequest();
        CommentVo commentVo = commentService.queryComment(object.getLong("comment_id"));
        if (commentVo == null) {
            return ResultMap.error("回复失败,不存在评论");
        }
        commentVo.reply(object);
        int result = commentService.doSave(commentVo);
        return result > 0 ? ResultMap.ok("回复成功") : ResultMap.error("回复失败,可能已经回复过了");
    }

    @PostMapping("reply")
    public Object repyComment(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        JSONObject object = super.getJsonRequest();

        CommentVo commentVo = CommentVo.builder()
                .user_id(loginUser.getUserId())
                .build();
        commentVo.reply(object);
        int result = commentService.updateComment(commentVo);
        return result > 0 ? ResultMap.ok("评论添加成功") : ResultMap.error("评论添加失败");
    }

    /**
     * 评论数量
     *
     * @param typeId  类型
     * @param valueId 产品
     * @return
     */
    @ApiOperation(value = "评论数量")
    @GetMapping("count")
    public Object count(Integer typeId, Integer valueId) {
        Map<String, Object> resultObj = Maps.newHashMap();
        //
        Map param = Maps.newHashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        Integer allCount = commentService.queryTotal(param);
//        Integer hasPicCount = commentService.queryhasPicTotal(param);
        resultObj.put("allCount", allCount);
        resultObj.put("hasPicCount", 0);
        return toResponsSuccess(resultObj);
    }

    /**
     * @param typeId
     * @param valueId
     * @param showType 选择评论的类型 0 全部， 1 只显示图片
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "选择评论类型")
    @IgnoreAuth
    @GetMapping("list")
    public Object list(Integer typeId,
                       Integer valueId,
                       Integer showType,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort,
                       String order) {
        Map param = Maps.newHashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        param.put("page", page);
        param.put("limit", size);
        param.put("order", StringUtils.isNullOrEmpty(sort) ? "desc" : sort);
        param.put("sidx", StringUtils.isNullOrEmpty(sort) ? "id" : order);
        param.put("hasPic", showType);
        //查询列表数据
        PageHelper.startPage(page, size);
        List<CommentVo> commentList = commentService.queryCommentList(param);

        PageInfo pageInfo = new PageInfo<>(commentList);
        PageUtils commenData = new PageUtils(pageInfo);
//        for (CommentVo commentItem : commentList) {
//            commentItem.setContent(Base64.decode(commentItem.getContent()));
//            commentItem.setUser_info(userService.queryObject(commentItem.getUser_id()));
//
//            Map paramPicture = new HashMap();
//            paramPicture.put("comment_id", commentItem.getId());
//            List<CommentPictureVo> commentPictureEntities = commentPictureService.queryList(paramPicture);
//            commentItem.setPic_list(commentPictureEntities);
//        }
        return ResultMap.response(ResultCodeEnum.SUCCESS, commenData);
    }

    @GetMapping("mer-list")
    public Object merList(@LoginUser LoginUserVo userVo,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                          @RequestParam(value = "status", defaultValue = "-1") Integer status) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        LoginUserVo sysUserVo = userService.queryByUserId(userVo.getUserId());
        if (sysUserVo == null) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }

        Map param = Maps.newHashMap();
        param.put("page", page);
        param.put("limit", size);
        param.put("sidx", "id");
        param.put("order", "desc");
        PageHelper.startPage(page, size);
        List<CommentVo> commentList = commentService.queryCommentList(param);
        PageInfo pageInfo = new PageInfo<>(commentList);
        PageUtils commenData = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, commenData);
    }
}
