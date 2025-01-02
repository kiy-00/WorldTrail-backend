package com.ruoyi.word.repository;

import com.ruoyi.word.entity.userWords.UserLexicon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface LexiconRepository extends MongoRepository<UserLexicon, String> {

    List<UserLexicon> findByUserId(Long userId);
    List<UserLexicon> findByUserIdAndLexiconId(Long userId, String lexiconId);
    Optional<UserLexicon> findByUserIdAndLexiconIdAndWordId(Long userId, String lexiconId, String wordId);
}
