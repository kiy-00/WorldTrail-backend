package com.ruoyi.word.controller;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.word.service.LexiconService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "词库管理", description = "用户词库相关接口")
@RestController
@RequestMapping("/word")
public class LexiconController {

    private final LexiconService lexiconService;

    public LexiconController(LexiconService lexiconService) {
        this.lexiconService = lexiconService;
    }


    @Operation(summary = "获取用户词书", description = "获取指定用户的所有词书统计信息")
    @ApiResponse(responseCode = "200", description = "成功获取词书统计信息")
    @GetMapping("/api/lexicon")
    public List<Map<String, Object>> getUserLexicons(
            @Parameter(description = "用户认证信息") Principal principal) {
        Long userId = SecurityUtils.getUserId();
        return lexiconService.getLexiconStatistics(userId);
    }

    @Operation(summary = "获取词书进度", description = "获取指定用户特定词书的学习进度")
    @ApiResponse(responseCode = "200", description = "成功获取词书进度")
    @GetMapping("/api/lexicon/progress")
    public Map<String, Object> getLexiconProgress(
            @Parameter(description = "用户认证信息") Principal principal,
            @Parameter(description = "词书名称", required = true)
            @RequestHeader("lexiconId") String lexiconId) {
        Long userId = SecurityUtils.getUserId();
        return lexiconService.getLexiconProgress(lexiconId, userId);
    }

    @GetMapping("/api/lexicon/count")
    public ResponseEntity<List<Map<String, Object>>> getWordCounts(
            @RequestHeader("lexiconId") String lexiconId) {
        Long userId = SecurityUtils.getUserId();
        List<Map<String, Object>> result = lexiconService.getWordCountsByUserAndLexicon(userId, lexiconId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "选择词书", description = "将指定词书同步到用户的词库中")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "词书选择成功",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "请求参数无效或词书不存在",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/api/lexicon/select")
    public ResponseEntity<Map<String, String>> selectBook(@RequestBody Map<String, String> request, @Parameter(description = "用户认证信息") Principal principal) {
        String bookId = request.get("bookId");

        // 验证请求参数
        if (bookId == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Book id is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Long userId = SecurityUtils.getUserId();
        // 调用服务方法，将词书数据同步到用户词书表
        boolean success = lexiconService.addBookToUserLexicon(userId, bookId);


        if (success) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book selected successfully");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to select book or book does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "删除用户词书", description = "根据词书名从用户词书表中删除指定词书")
    @DeleteMapping("/api/lexicon/delete")
    public ResponseEntity<Map<String, String>> deleteBook(
            @RequestBody Map<String, String> request,
            @Parameter(description = "用户认证信息") Principal principal) {
        String bookId = request.get("bookId");

        // 验证请求参数
        if (bookId == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Book name is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // 获取用户 ID
        Long userId = SecurityUtils.getUserId();

        // 调用服务层方法执行删除操作
        boolean success = lexiconService.deleteBooksFromUserLexicon(userId, bookId);

        if (success) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete book or book does not exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}