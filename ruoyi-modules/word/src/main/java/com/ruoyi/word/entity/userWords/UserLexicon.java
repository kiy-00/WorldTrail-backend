package com.ruoyi.word.entity.userWords;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "userLexicon")
public class UserLexicon {
    @Id
    private String id; // MongoDB 的 _id 字段
    private Long userId;
    private String lexiconId;
    private String wordId;
    private LocalDate lastLearnTime;
    private int count;
    public UserLexicon(Long userId, String lexiconId, String wordId, LocalDate lastLearnTime,int count) {
        this.userId = userId;
        this.lexiconId = lexiconId;
        this.wordId = wordId;
        this.lastLearnTime = lastLearnTime;
        this.count = count;
    }
    public String getId() {
        return id;
    }

    public int getReviewCount(){
        return count;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLexiconId() {
        return lexiconId;
    }

    public String getWordId() {
        return wordId;
    }

    public LocalDate getLastLearnTime() {
        return lastLearnTime;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLastLearnTime(LocalDate lastLearnTime) {
        this.lastLearnTime = lastLearnTime;
    }

    @Override
    public String toString() {
        return "UserLexicon{" +
                "userId='" + userId + '\'' +
                ", lexiconName='" + lexiconId + '\'' +
                ", wordId='" + wordId + '\'' +
                ", count=" + count +
                '}';
    }

}

