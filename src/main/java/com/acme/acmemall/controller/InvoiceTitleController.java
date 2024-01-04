package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.model.InvoiceTitleVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IInvoiceTitleService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:发票抬头
 * @author: ihpangzi
 * @time: 2024/1/4 16:45
 */
@RestController
@RequestMapping("/api/invoice-title")
public class InvoiceTitleController extends ApiBase {
    @Autowired
    IInvoiceTitleService invoiceTitleService;

    @GetMapping("list")
    public Object invoiceTitleList(@LoginUser LoginUserVo loginUser) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        List<InvoiceTitleVo> invoiceTitleList = invoiceTitleService.queryInvoiceTitleList(param);
        return toResponsSuccess(invoiceTitleList);
    }

    @PostMapping("save")
    public Object save(@LoginUser LoginUserVo loginUser) {
        JSONObject request = this.getJsonRequest();
        InvoiceTitleVo entity = new InvoiceTitleVo();
        if (request != null) {
            entity.setUserId(loginUser.getUserId());
            entity.setType(Integer.parseInt(request.getString("type")));
            entity.setInvoiceType(request.getInteger("invoiceType"));
            entity.setTitle(request.getString("title"));
            entity.setTaxNumber(request.getString("taxNumber"));
            entity.setCompanyAddress(request.getString("companyAddress"));
            entity.setTelephone(request.getString("telephone"));
            entity.setBankName(request.getString("bankName"));
            entity.setBankAccount(request.getString("bankAccount"));
            entity.setEmail(request.getString("email"));
            entity.setCompanyTel(request.getString("companyTel"));
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", loginUser.getUserId());

        List<InvoiceTitleVo> addressEntities = invoiceTitleService.queryInvoiceTitleList(param);
        if (addressEntities.size() == 0) {//第一次添加设置为默认开票抬头
            entity.setIs_default(1);
        }
        if (entity.getIs_default() == 1) {
            entity = new InvoiceTitleVo();
            entity.setUserId(loginUser.getUserId());
            entity.setIs_default(0);
            invoiceTitleService.updateIsDefault(entity);
        }
        if (null == entity.getId() || entity.getId() == 0) {
            entity.setId(null);
            invoiceTitleService.save(entity);
        } else {
            invoiceTitleService.update(entity);
        }
        return toResponsSuccess(entity);
    }


    @GetMapping("detail")
    public Object detail(Integer id, @LoginUser LoginUserVo loginUser) {
        InvoiceTitleVo entity = invoiceTitleService.queryObject(id);
        if (entity == null) {
            return toResponsSuccess(null);
        }
        //判断越权行为，智能删除用户自己的
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权查看", "");
        }
        return toResponsSuccess(entity);
    }
}
