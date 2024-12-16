package com.ruoyi.word.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.word.entity.words.Word;
import com.ruoyi.word.repository.WordRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WordService {

    private final WordRepository wordRepository;


    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void addWord(Word word) {
        wordRepository.save(word);
    }


    public Optional<Word> findById(ObjectId id) {
        return wordRepository.findById(id);
    }
    public Iterable<Word> findByIds(List<ObjectId> ids) {
        return wordRepository.findAllById(ids);
    }
}