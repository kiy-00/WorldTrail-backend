package com.ruoyi.word.entity.words;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
@Data
@Document(collection = "books")
public class Book {
    @Id
    ObjectId id;
    String language;
    String bookName;
    String description;
    Long createUser;
    Set<String> words;
    @Transient
    int wordCount;
    public int getWordCount() {
        return words != null ? words.size() : 0;
    }

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public Book(String language, String bookName, String description, Long createUser, Set<String> words) {
        this.language = language;
        this.bookName = bookName;
        this.description = description;
        this.createUser = createUser;
        this.words = words;
    }

    public String getLanguage() {
        return language;
    }

    public String getBookName() {
        return bookName;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public String getId() {
        return id.toHexString();
    }


    @Override
    public String toString() {
        return "Category{" +
                "language='" + language + '\'' +
                ", bookName='" + bookName + '\'' +
                ", wordCount=" + wordCount +
                '}';
    }
}
