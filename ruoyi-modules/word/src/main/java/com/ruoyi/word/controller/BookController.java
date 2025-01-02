package com.ruoyi.word.controller;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.word.entity.dto.BookDTO;
import com.ruoyi.word.entity.words.Book;
import com.ruoyi.word.service.BookService;
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


import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "词书管理", description = "词书相关的CRUD接口")
@RestController
@RequestMapping("/word/books")
public class BookController {

    private final BookService bookService;
    private final WordService wordService;

    public BookController(BookService bookService,WordService wordService) {
        this.bookService = bookService;
        this.wordService = wordService;
    }

    @Operation(summary = "获取用户的所有词书", description = "获取当前登录用户创建的所有词书")
    @ApiResponse(responseCode = "200", description = "成功获取词书列表")
    @GetMapping()
    public List<Book> getBooksByCreateUser(
            @Parameter(description = "用户认证信息") Principal principal) {
        Long createUser = SecurityUtils.getUserId();
        return bookService.getBooksByCreateUser(createUser);
    }

    @Operation(summary = "按语言获取词书", description = "获取当前登录用户创建的指定语言的词书")
    @ApiResponse(responseCode = "200", description = "成功获取词书列表")
    @GetMapping("/by-language")
    public List<Book> getBooksByCreateUserAndLanguage(
            @Parameter(description = "用户认证信息") Principal principal,
            @Parameter(description = "词书语言", required = true) @RequestParam String language) {
        Long createUser = SecurityUtils.getUserId();
        return bookService.getBooksByCreateUserAndLanguage(createUser, language);
    }

    @Operation(summary = "获取词书详情", description = "根据词书ID获取详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取词书信息"),
            @ApiResponse(responseCode = "404", description = "词书不存在"),
            @ApiResponse(responseCode = "400", description = "无效的ID格式")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @Parameter(description = "词书ID", required = true) @PathVariable ObjectId id) {
        try {
            Optional<Book> book = bookService.findById(id);
            if (book.isPresent()) {
                List<ObjectId> objectIdList = book.get().getWords().stream()
                        .map(ObjectId::new)             // 将合法的字符串转换为 ObjectId
                        .collect(Collectors.toList());

                return ResponseEntity.ok(wordService.findByIds(objectIdList));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Word with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID format: " + id);
        }
    }

    @Operation(summary = "创建新词书", description = "创建一个新的词书")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "词书创建成功"),
            @ApiResponse(responseCode = "400", description = "无效的请求数据")
    })
    @PostMapping("/add")
    public ResponseEntity<Book> createBook(
            @Parameter(description = "词书信息", required = true) @RequestBody BookDTO bookDTO,
            @Parameter(description = "用户认证信息") Principal principal) {
        Long createUser = SecurityUtils.getUserId();
        Book createdBook = bookService.createBook(bookDTO, createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @Operation(summary = "添加单词到词书", description = "向指定词书中添加单词")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "单词添加成功或已存在"),
            @ApiResponse(responseCode = "403", description = "没有权限修改此词书")
    })
    @PutMapping("/{bookId}/add-word/{wordId}")
    public ResponseEntity<String> addWordToBook(
            @Parameter(description = "词书ID", required = true) @PathVariable ObjectId bookId,
            @Parameter(description = "单词ID", required = true) @PathVariable String wordId,
            @Parameter(description = "用户认证信息") Principal principal) {
        try {
            Long createUser = SecurityUtils.getUserId();
            boolean updated = bookService.addWordToBook(bookId, wordId, createUser);
            if (updated) {
                return ResponseEntity.ok("Word added successfully.");
            } else {
                return ResponseEntity.ok("Word already exists in the book.");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to modify this book.");
        }
    }

    @Operation(summary = "从词书移除单词", description = "从指定词书中移除单词")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "单词移除成功或不存在"),
            @ApiResponse(responseCode = "403", description = "没有权限修改此词书")
    })
    @PutMapping("/{bookId}/remove-word/{wordId}")
    public ResponseEntity<String> removeWordFromBook(
            @Parameter(description = "词书ID", required = true) @PathVariable ObjectId bookId,
            @Parameter(description = "单词ID", required = true) @PathVariable String wordId,
            @Parameter(description = "用户认证信息") Principal principal) {
        try {
            Long createUser = SecurityUtils.getUserId();
            boolean updated = bookService.removeWordFromBook(bookId, wordId, createUser);
            if (updated) {
                return ResponseEntity.ok("Word removed successfully.");
            } else {
                return ResponseEntity.ok("Word not found in the book.");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to modify this book.");
        }
    }

    @Operation(summary = "删除词书", description = "删除指定的词书")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "词书删除成功"),
            @ApiResponse(responseCode = "403", description = "没有权限删除此词书"),
            @ApiResponse(responseCode = "404", description = "词书不存在")
    })
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(
            @Parameter(description = "词书ID", required = true) @PathVariable ObjectId bookId,
            @Parameter(description = "用户认证信息") Principal principal) {
        try {
            Long currentUserId = SecurityUtils.getUserId();
            boolean deleted = bookService.deleteBook(bookId, currentUserId);
            if (deleted) {
                return ResponseEntity.ok("Book deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found or you do not have permission to delete this book.");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this book.");
        }
    }
    @GetMapping("/by-word/{wordId}")
    public List<Book> getBooksByWord(@PathVariable String wordId) {
        return bookService.findBooksByWord(wordId);
    }
}