package com.ruoyi.word.controller;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.word.dto.BookDTO;
import com.ruoyi.word.entity.words.Book;
import com.ruoyi.word.service.BookService;
import com.ruoyi.word.service.UserService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/word/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    // 获取所有指定 createUser 的词书
    @GetMapping()
    public List<Book> getBooksByCreateUser() {
        String createUser = SecurityUtils.getUsername();
        System.out.println("createUser: " + createUser);
        return bookService.getBooksByCreateUser(createUser);
    }
    @GetMapping("/by-language")
    public List<Book> getBooksByCreateUserAndLanguage(
            @RequestParam String language) {
        String createUser = SecurityUtils.getUsername();
        return bookService.getBooksByCreateUserAndLanguage(createUser, language);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable ObjectId id) {
        try {

            Optional<Book> book = bookService.findById(id);

            if (book.isPresent()) {
                return ResponseEntity.ok(book.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Word with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID format: " + id);
        }
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) {

        String createUser = SecurityUtils.getUsername();
        Book createdBook = bookService.createBook(bookDTO, createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }
    @PutMapping("/{bookId}/add-word/{wordId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> addWordToBook(
            @PathVariable ObjectId bookId,
            @PathVariable String wordId) {
        try{
            String createUser = SecurityUtils.getUsername();
            boolean updated = bookService.addWordToBook(bookId, wordId,createUser);
            if (updated) {
                return ResponseEntity.ok("Word added successfully.");
            } else {
                return ResponseEntity.ok("Word already exists in the book.");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to modify this book.");
        }
    }
    @PutMapping("/{bookId}/remove-word/{wordId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> removeWordFromBook(
            @PathVariable ObjectId bookId,
            @PathVariable String wordId) {
        try {
            String createUser = SecurityUtils.getUsername();

            boolean updated = bookService.removeWordFromBook(bookId, wordId, createUser);

            if (updated) {
                return ResponseEntity.ok("Word removed successfully.");
            } else {
                return ResponseEntity.ok("Word not found in the book.");
            }
        } catch (SecurityException e) {
            // 用户无权限修改书籍，返回 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to modify this book.");
        }
    }
    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> deleteBook(
            @PathVariable ObjectId bookId) {
        try {
            // 获取当前用户的 ID
            String currentUserId = SecurityUtils.getUsername();

            // 调用服务层方法删除 book
            boolean deleted = bookService.deleteBook(bookId, currentUserId);

            if (deleted) {
                return ResponseEntity.ok("Book deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found or you do not have permission to delete this book.");
            }
        } catch (SecurityException e) {
            // 用户无权限删除书籍，返回 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this book.");
        }
    }
}