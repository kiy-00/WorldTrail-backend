package com.ruoyi.word.controller;

import com.ruoyi.word.entity.words.PartOfSpeech;
import com.ruoyi.word.entity.words.Phonetics;
import com.ruoyi.word.entity.words.Word;
import com.ruoyi.word.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "单词管理", description = "与单词相关的操作接口")
@RestController
@RequestMapping("/words")
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @Operation(summary = "测试添加单词", description = "添加一个测试单词到数据库")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "单词添加成功"),
            @ApiResponse(responseCode = "400", description = "请求参数有误")
    })
    @PostMapping("/test")
    public String testWord() {
        // Java 8 兼容写法
        Word word = new Word(
                "example",
                "en",
                Collections.singletonList(new Phonetics("/ɪɡˈzɑːmp(ə)l/", "audio")),
                Collections.singletonList(new PartOfSpeech("n.", "example definition", null, null, null))
        );
        wordService.addWord(word);
        return word.toString();
    }

    @Operation(summary = "通过ID获取单词", description = "根据单词的ID获取对应的单词信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取单词"),
            @ApiResponse(responseCode = "400", description = "无效的ID格式"),
            @ApiResponse(responseCode = "404", description = "未找到对应ID的单词")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getWordById(
            @Parameter(description = "单词ID", required = true)
            @PathVariable("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);

            Optional<Word> word = wordService.findById(objectId);

            if (word.isPresent()) {
                return ResponseEntity.ok(word.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Word with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID format: " + id);
        }
    }
    @GetMapping("/by-ids")
    public Iterable<Word> getWordsByIds(@RequestParam List<String> ids) {
        List<ObjectId> objectIds = ids.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        return wordService.findByIds(objectIds);
    }

}