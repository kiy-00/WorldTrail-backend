package com.ruoyi.word.dto;


import java.util.Set;

public class BookDTO {
    private String language;
    private String bookName;
    private String description;
    private Set<String> words;

    public BookDTO(String language, String bookName, String description, Set<String> words) {
        this.language = language;
        this.bookName = bookName;
        this.description = description;
        this.words = words;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }
}