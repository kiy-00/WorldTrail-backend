package com.ruoyi.word.repository;

import com.ruoyi.word.entity.words.Word;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends MongoRepository<Word, ObjectId> {

    // 添加随机获取单词的方法
    @Aggregation(pipeline = {"{$sample: {size: 1}}"})
    Word findRandomWord();
}

