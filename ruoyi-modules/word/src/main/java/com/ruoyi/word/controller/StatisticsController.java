package com.ruoyi.word.controller;

import com.ruoyi.word.entity.userWords.Log;
import com.ruoyi.word.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "统计管理", description = "用户统计相关接口")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }


    @Operation(summary = "添加日志", description = "根据请求头添加用户日志")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "日志添加成功"),
            @ApiResponse(responseCode = "400", description = "请求参数有误")
    })
    @PostMapping("/addLog")
    public String addLog(
            @Parameter(description = "用户认证信息") Principal principal,
            @Parameter(description = "词典", required = true) @RequestHeader("lexicon") String lexicon,
            @Parameter(description = "单词ID", required = true) @RequestHeader("wordId") String wordId)
    {
        Long userId = Long.parseLong(principal.getName());
        Log log0 = new Log(userId, lexicon, wordId, LocalDate.now());

        statisticsService.addLog(log0);
        statisticsService.updateLexicon(userId, lexicon, wordId, LocalDate.now());
        return log0.toString();
    }

    @Operation(summary = "获取每周统计", description = "获取指定用户的每周统计数据")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取统计数据"),
            @ApiResponse(responseCode = "400", description = "请求参数有误")
    })
    @GetMapping("/weekly")
    public List<Map<String, Object>> getWeeklyStatistics(
            @Parameter(description = "用户认证信息") Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return statisticsService.getWeeklyStatistics(userId);
    }
}