package com.acme.acmemall.cainiao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cainiao.logistics.cloud")
@Data
public class LogisticsCloudProperties {
    private String appId;
    private String appSecret;
    private String queryExpressRoutes;
    private String interceptPackage;
//    private List<Map<String, String>> apiList = Lists.newArrayList();
}
