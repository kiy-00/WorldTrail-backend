package com.ruoyi.word.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "word")
public class Word {
    @Id
    private String id;
    private String content;
    private Long creatorId;
    private String imgPath;
    // other fields, getters, and setters
}
