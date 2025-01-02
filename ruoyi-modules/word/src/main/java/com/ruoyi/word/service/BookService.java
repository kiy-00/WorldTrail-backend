package com.ruoyi.word.service;

import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.word.entity.dto.BookDTO;
import com.ruoyi.word.entity.words.Book;
import com.ruoyi.word.repository.BookRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    // 获取所有指定 createUser 的词书
    public List<Book> getBooksByCreateUser(Long createUser) {
        return bookRepository.findByCreateUserIn(Arrays.asList(createUser, Constants.DEFAULT_CREATOR));
    }
    public Optional<Book> findById(ObjectId id) {
        return bookRepository.findById(id);
    }
    // 获取所有指定 createUser 和 language 的词
    public List<Book> getBooksByCreateUserAndLanguage(Long createUser, String language) {
        return  bookRepository.findByCreateUserInAndLanguage(Arrays.asList(createUser,Constants.DEFAULT_CREATOR), language);
    }

    public Book createBook(BookDTO bookDTO, Long createUser) {
        // 构建 Book 实体对象
        Book book = new Book(bookDTO.getLanguage(),bookDTO.getBookName(),bookDTO.getDescription(),createUser,bookDTO.getWords());

        return bookRepository.save(book);
    }

    public boolean addWordToBook(ObjectId bookId, String wordId,Long createUser) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {

            Book book = optionalBook.get();
            if(!book.getCreateUser().equals(createUser))
                throw new SecurityException("You do not have permission to modify this book");

            boolean isAdded = book.getWords().add(wordId);

            if (isAdded) {
                bookRepository.save(book);
            }

            return isAdded; // 返回 true 表示更新成功，false 表示 wordId 已存在
        } else {
            throw new RuntimeException("Book not found with id: " + bookId);
        }
    }
    public boolean removeWordFromBook(ObjectId bookId, String wordId, Long createUser) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();


            if (!book.getCreateUser().equals(createUser)) {
                throw new SecurityException("You do not have permission to modify this book.");
            }

            boolean isRemoved = book.getWords().remove(wordId);

            if (isRemoved) {
                bookRepository.save(book);
            }

            return isRemoved;
        } else {
            throw new RuntimeException("Book not found with id: " + bookId);
        }
    }
    public boolean deleteBook(ObjectId bookId, Long currentUserId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (!book.getCreateUser().equals(currentUserId)) {
                throw new SecurityException("You do not have permission to delete this book.");
            }

            bookRepository.delete(book);
            return true;
        } else {
            return false;
        }
    }
    public List<Book> findBooksByWord(String wordId) {
        return bookRepository.findByWord(wordId);
    }

}
