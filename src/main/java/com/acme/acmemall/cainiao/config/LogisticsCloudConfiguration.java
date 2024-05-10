/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.cainiao.config;

import com.acme.acmemall.cainiao.service.LogisticsCloudService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogisticsCloudProperties.class)
public class LogisticsCloudConfiguration {

    private final LogisticsCloudProperties properties;

    public LogisticsCloudConfiguration(LogisticsCloudProperties properties) {
        this.properties = properties;
    }

    @Bean
    public LogisticsCloudService logisticsCloudService() {
        LogisticsCloudService logicCloudService = new LogisticsCloudService();
        logicCloudService.setProperties(properties);
        return logicCloudService;
    }
}
