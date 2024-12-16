package com.ruoyi.word.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ruoyi.word.entity.words.Word;

public interface WordRepository extends MongoRepository<Word, ObjectId> {
}