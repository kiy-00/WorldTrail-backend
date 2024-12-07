package com.ruoyi.word.service;

import com.ruoyi.word.domain.Word;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WordInfoService {
    List<Word> selectWordByCreatorId(Long id);
    Integer insertWord(Word word, MultipartFile file);
    Integer deleteWordById(String id);
}
