package com.ruoyi.word.controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.word.entity.words.PartOfSpeech;
import com.ruoyi.word.entity.words.Phonetics;
import com.ruoyi.word.entity.words.Word;
import com.ruoyi.word.service.WordService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/word/words")
public class WordController {
    final
    WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }


    @PostMapping("/test")
    public String testWord() {
        // 原本的代码（Java 9+）
// Word word = new Word("example", "en", List.of("CET4", "CET6"),
//         List.of(new Phonetics("/ɪɡˈzɑːmp(ə)l/", "audio")),
//         List.of(new PartOfSpeech("n.", "example definition", null, null, null)));

// Java 8 兼容写法
        Word word = new Word(
                "abandon",
                "en",
                Collections.singletonList(new Phonetics("/əˈbændən/", "audio")),
                Collections.singletonList(new PartOfSpeech("n.", "放任，放纵", null, null, null))
        );
        wordService.addWord(word);
        return word.toString();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getWordById(@PathVariable("id") ObjectId id) {
        try {

            Optional<Word> word = wordService.findById(id);

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
