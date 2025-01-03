package com.ruoyi.system.api;

import com.ruoyi.system.api.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient(name = "openai",configuration = FeignConfiguration.class, url = "https://dashscope.aliyuncs.com/compatible-mode/v1")
public interface OpenAIClient {

    @PostMapping(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE)
    String checkContent(@RequestBody Object request, @RequestHeader("Authorization") String token);
}