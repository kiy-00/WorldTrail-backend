package com.ruoyi.admin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.admin.entities.ContentCheckRequest;
import com.ruoyi.admin.entities.Report;
import com.ruoyi.admin.mapper.ReportMapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.OpenAIClient;
import com.ruoyi.system.api.RemoteForumService;
import com.ruoyi.system.api.model.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    RemoteForumService remoteForumService;
    @Autowired
    private OpenAIClient openAIClient;

    public Integer postReport(Report report) {
        report.setUserId(SecurityUtils.getUserId());
        report.setCreatedTime(new Date());
        report.setUpdatedTime(new Date());
        if(report.getType()=='1'||report.getType()=='2')
            CompletableFuture.runAsync(() -> AIReview(report.getTargetId(), report.getType()));
        return reportMapper.insert(report);
    }

    public List<Report> listReports() {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getStatus, '0');
        List<Report> list = reportMapper.selectList(wrapper);
        return list;
    }

    public Integer updateReport(Long id, String reply) {
        Report report = reportMapper.selectById(id);
        report.setReply(reply);
        report.setStatus('1');
        report.setUpdatedTime(new Date());
        return reportMapper.updateById(report);
    }

    public void AIReview(Long id,Character type){
        if(type=='1'){
            String content = remoteForumService.getPostContent(id, SecurityConstants.INNER).getData();
            String raw=checkContent(content);
            Map<String, String> result = getAIResponse(raw);
            if(result.get("is_violation").equals("true")){
                remoteForumService.updatePost(new StatusDTO(id, '2'), SecurityConstants.INNER);
            }
        }
        else if(type=='2'){
            String content = remoteForumService.getCommentContent(id, SecurityConstants.INNER).getData();
            String raw=checkContent(content);
            Map<String, String> result = getAIResponse(raw);
            if(result.get("is_violation").equals("true")){
                remoteForumService.updateComment(new StatusDTO(id, '2'), SecurityConstants.INNER);
            }
        }
    }
    public Map<String, String> getAIResponse(String jsonResponse) {
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
            Map<String,String> result = new HashMap<>();
            result.put("is_violation",String.valueOf(isViolation));
            result.put("violation_types",String.join(",",violationTypes));
            result.put("reason",reason);
            result.put("suggestion",suggestion);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String checkContent(String text) {
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个内容审核助手，需要判断用户输入的内容是否违规。请从以下几个方面进行判断：\n1. 垃圾广告\n2. 色情暴力\n3. 攻击谩骂\n4. 其他违规\n\n请以JSON格式返回结果，包含以下字段：\n- is_violation: 是否违规 (true/false)\n- violation_types: 违规类型列表\n- reason: 违规原因\n- suggestion: 处理建议");

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", "请审核以下内容：\n" + text);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(systemMessage);
            messages.add(userMessage);
            ContentCheckRequest request = new ContentCheckRequest(
                    "qwen-long",
                    messages,
                    0.1,
                    2000
            );

            return openAIClient.checkContent(request,"Bearer sk-8c750dee97b84b929e2f6bd78276e31c");
    }
}
