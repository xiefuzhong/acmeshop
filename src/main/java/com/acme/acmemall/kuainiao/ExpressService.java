package com.acme.acmemall.kuainiao;

import com.acme.acmemall.kuainiao.config.ExpressProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 18:47
 */
@Slf4j
public class ExpressService {
    private static final String REQ_URL = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    private static final String REQUEST_TYPE = "8001";
    private ExpressProperties properties;

    public ExpressProperties getProperties() {
        return properties;
    }

    public void setProperties(ExpressProperties properties) {
        this.properties = properties;
    }

    @Resource
    RestTemplate restTemplate;

    public String query(String expressCode, String logisticCode) {
        LinkedMultiValueMap<String, String> param = parseParam(expressCode, logisticCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
        String response = null;
        try {
            response = restTemplate.postForObject(REQ_URL, request, String.class);
        } catch (RestClientException e) {
            log.error("查询报错:" + Throwables.getStackTraceAsString(e));
        }
        return response;
    }

    private LinkedMultiValueMap<String, String> parseParam(String expressCode, String logisticCode) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ShipperCode", expressCode);
        map.put("LogisticCode", logisticCode);
        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        String jsonStr = null;
        String DataSign = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonStr = mapper.writeValueAsString(map);
            DataSign = Base64.encodeBase64String(DigestUtils.md5Hex((jsonStr + properties.getAppKey()).getBytes()).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.add("RequestType", REQUEST_TYPE);
        param.add("EBusinessID", properties.getAppId());
        param.add("RequestData", jsonStr);
        param.add("DataSign", DataSign);
        param.add("DataType", "2");
        return param;
    }


}
