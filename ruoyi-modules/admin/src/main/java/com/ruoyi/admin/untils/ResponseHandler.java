package com.ruoyi.admin.untils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class ResponseHandler {
    public void parseResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // 获取choices数组中第一个元素
            JsonNode choice = root.get("choices").get(0);

            // 获取message内容并解析内部的JSON字符串
            String content = choice.get("message").get("content").asText();
            // 去除```json和```标记
            content = content.replaceAll("```json\\s*|```\\s*", "");
            JsonNode contentJson = mapper.readTree(content);

            // 获取各字段
            boolean isViolation = contentJson.get("is_violation").asBoolean();
            String[] violationTypes = mapper.convertValue(
                    contentJson.get("violation_types"),
                    String[].class
            );
            String reason = contentJson.get("reason").asText();
            String suggestion = contentJson.get("suggestion").asText();

            // 获取其他响应信息
            String model = root.get("model").asText();
            int totalTokens = root.get("usage").get("total_tokens").asInt();

            // 使用解析后的数据...

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
