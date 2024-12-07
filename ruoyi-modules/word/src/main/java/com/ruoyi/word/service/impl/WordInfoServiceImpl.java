package com.ruoyi.word.service.impl;

import com.ruoyi.common.core.utils.uuid.IdUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysFile;
import com.ruoyi.word.domain.Word;
import com.ruoyi.word.service.WordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WordInfoServiceImpl implements WordInfoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    RemoteFileService remoteFileService;
    @Override
    public List<Word> selectWordByCreatorId(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("creatorId").is(id));
        return mongoTemplate.find(query, Word.class);
    }

    @Override
    public Integer insertWord(Word word, MultipartFile file) {
        word.setCreatorId(SecurityUtils.getUserId());
        SysFile fileInfo= remoteFileService.upload(file).getData();
        word.setImgPath(fileInfo.getUrl());
        mongoTemplate.insert(word);
        return 1;
    }

    @Override
    public Integer deleteWordById(String id) {
        Word word = mongoTemplate.findById(id, Word.class);
        if (word == null) {
            return 0;
        }
        if(word.getCreatorId().equals(SecurityUtils.getUserId())){
            mongoTemplate.remove(word);
            return 1;
        }
        else
            return -1;
    }
}
