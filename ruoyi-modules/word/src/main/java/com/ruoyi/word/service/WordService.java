package com.ruoyi.word.service;

import com.ruoyi.word.entity.words.Word;
import com.ruoyi.word.repository.WordRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordService {

    private final WordRepository wordRepository;


    public long getTotalWordsCount() {
        return wordRepository.count();
    }

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }


    public Word addWord(Word word) {

        return wordRepository.save(word);  // 保存单个文档
    }
    public Optional<Word> findById(ObjectId id) {
        return wordRepository.findById(id);
    }
    public Iterable<Word> findByIds(List<ObjectId> ids) {
        return wordRepository.findAllById(ids);
    }

}