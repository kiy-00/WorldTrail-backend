package com.ruoyi.system.api.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class FeignConfiguration {

    @Bean(name = "customFeignRequestInterceptor")
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            Collection<String> authHeaders = template.headers().get("Authorization");
            if (authHeaders != null && !authHeaders.isEmpty()) {
                // 获取原始 Authorization header
                String originalAuth = authHeaders.iterator().next();
                // 提取 sk- 开头的部分
                if (originalAuth != null && originalAuth.contains(",")) {
                    String[] list = originalAuth.split(",");
                    // 确保是以 sk- 开头
                    if (list.length > 1 && list[0].startsWith("Bearer sk-")) {
                        // 清除原有的 Authorization header
                        template.removeHeader("Authorization");
                        // 只设置 sk 部分
                        template.header("Authorization", list[0]);
                    }
                }
            }
        };
    }
}
