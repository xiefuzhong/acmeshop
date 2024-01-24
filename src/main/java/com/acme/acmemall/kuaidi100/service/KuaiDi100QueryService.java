package com.acme.acmemall.kuaidi100.service;

import com.acme.acmemall.kuaidi100.request.QueryTrackParam;
import com.acme.acmemall.kuaidi100.request.QueryTrackRequest;
import com.acme.acmemall.kuaidi100.response.QueryTrackResp;
import com.acme.acmemall.kuaidi100.utils.HttpResult;
import com.acme.acmemall.kuaidi100.utils.HttpUtils;
import com.acme.acmemall.kuaidi100.utils.PropertiesReader;
import com.acme.acmemall.kuaidi100.utils.SignUtils;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 10:24
 */
@Service
public class KuaiDi100QueryService {
    protected Logger logger = Logger.getLogger(getClass());

    String key = PropertiesReader.get("kuaidi100.key");
    String customer = PropertiesReader.get("kuaidi100.customer");
    String queryTrackUrl = PropertiesReader.get("kuaidi100.query-track");

    private static final QueryTrackResp EMPTY_RESULT = new QueryTrackResp();

    public QueryTrackResp queryTrack(Map<String, Object> params) throws Exception {
        logger.info("queryTrack:" + GsonUtil.toJson(params));
        QueryTrackParam queryTrackParam = QueryTrackParam.builder()
                .com(MapUtils.getString("com", params))
                .num(MapUtils.getString("num", params))
                .build();
        String param = queryTrackParam.toParam();
        QueryTrackRequest request = QueryTrackRequest.builder()
                .param(param)
                .customer(customer)
                .sign(SignUtils.querySign(param, key, customer))
                .build();
        HttpResult result = HttpUtils.doPost(queryTrackUrl, request, 3000, 3000);
        if (result.getStatus() == HttpStatus.SC_OK && StringUtils.isNotBlank(result.getBody())) {
            return GsonUtil.getGson().fromJson(result.getBody(), QueryTrackResp.class);
        }
        return EMPTY_RESULT;
    }
}
