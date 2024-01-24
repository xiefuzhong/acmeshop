package com.acme.acmemall.kuainiao.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 18:50
 */
@ConfigurationProperties(prefix = "kuainiao.express")
@Data
public class ExpressProperties {
    private boolean enabled = false;
    private String appId;
    private String appKey;

    private List<Map<String, String>> vendors = Lists.newArrayList();
}
