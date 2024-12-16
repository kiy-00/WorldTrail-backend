package com.ruoyi.word.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ruoyi.word.entity.words.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, ObjectId> {

    List<Book> findByCreateUserIn(List<String> createUsers);

    // 查询所有指定 createUser 和 language 的词书
    List<Book> findByCreateUserInAndLanguage(List<String> createUsers, String language);

}