package com.ruoyi.word.controller;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.word.entity.words.Word;
import com.ruoyi.word.service.UserStudyPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@Tag(name = "学习计划管理", description = "用户学习计划相关接口")
@RestController
@RequestMapping("/word/api/studyplan")
public class UserStudyPlanController {

    private final UserStudyPlanService userStudyPlanService;

    @Autowired
    public UserStudyPlanController(UserStudyPlanService userStudyPlanService) {
        this.userStudyPlanService = userStudyPlanService;
    }

    @Operation(summary = "获取待复习单词组", description = "获取指定用户特定词书中一组待复习的单词")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取待复习单词"),
            @ApiResponse(responseCode = "400", description = "请求参数有误")
    })
    @GetMapping("/reviewwords/{wordbookName}")
    public List<Word> getAGroupOfReviewWords(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName) {
        Long userId = SecurityUtils.getUserId();
        return userStudyPlanService.getAGroupOfReviewWords(userId, wordbookName);
    }

    @GetMapping("/reviewcount/{wordbookName}")
    public int getReviewCountOfWords(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName,
            @Parameter(description = "用户认证信息") Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return userStudyPlanService.getReviewWordCount(userId, wordbookName);
    }

    @GetMapping("/learnwords/{wordbookName}")
    public List<Word> getAGroupOfLearnWords(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName) {
        Long userId = SecurityUtils.getUserId();
        return userStudyPlanService.getAGroupOfToLearnWords(userId, wordbookName);
    }

    @GetMapping("/learncount/{wordbookName}")
    public int getToLearnCountOfWords(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName) {
        Long userId = SecurityUtils.getUserId();
        return userStudyPlanService.getToLearnWordCount(userId, wordbookName);
    }

    @Operation(summary = "重置学习次数", description = "将指定单词的学习次数重置为0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "学习次数重置成功"),
            @ApiResponse(responseCode = "400", description = "请求参数有误")
    })
    @PutMapping("/resetcount/{wordbookName}/{wordId}")
    public void resetReviewCount(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName,
            @Parameter(description = "单词ID", required = true)
            @PathVariable String wordId) {
        Long userId = SecurityUtils.getUserId();
        userStudyPlanService.resetReviewCount(userId, wordbookName, wordId);
    }

    @PutMapping("/decrementcount/{wordbookName}/{wordId}")
    public void decrementReviewCount(
            @Parameter(description = "词书名称", required = true)
            @PathVariable String wordbookName,
            @Parameter(description = "单词ID", required = true)
            @PathVariable String wordId) {
        Long userId = SecurityUtils.getUserId();
        userStudyPlanService.decrementReviewCount(userId, wordbookName, wordId);
    }
}