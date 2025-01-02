package com.ruoyi.word.repository;
import com.ruoyi.word.entity.words.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, ObjectId> {

    List<Book> findByCreateUserIn(List<Long> createUsers);

    // 查询所有指定 createUser 和 language 的词书
    List<Book> findByCreateUserInAndLanguage(List<Long> createUsers, String language);
    Book findByBookName(String bookName);


    @Query("{ 'words': ?0 }")
    List<Book> findByWord(String wordId);
    Book findById(String id);
}
