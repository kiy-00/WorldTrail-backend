package com.ruoyi.word.entity.userWords;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "log")
public class Log {
    @Id
    private ObjectId logId;
    private Long userId;
    private String lexiconName;
    private String wordId;
    private LocalDate date;

    public Log( Long userId, String lexiconName, String wordId, LocalDate date) {

        this.userId = userId;
        this.lexiconName = lexiconName;
        this.wordId = wordId;
        this.date = date;

    }

    public LocalDate getDate() {
        return date;
    }

    public ObjectId getLogId() {
        return logId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLexiconName() {
        return lexiconName;
    }

    public String getWordId() {
        return wordId;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId='" + logId + '\'' +
                ", userId='" + userId + '\'' +
                ", lexiconName='" + lexiconName + '\'' +
                ", wordId='" + wordId + '\'' +
                ", date=" + date +
                '}';
    }
}
