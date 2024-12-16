package com.ruoyi.word.entity.words;

import java.util.List;
public class PartOfSpeech {

    private String type;  // 词性类型，例如 "noun", "verb", "adjective"
    private String definitions;
    private List<String> exampleSentences;
    private List<String> gender;
    private List<String> pluralForms;

    public List<String> getExampleSentences() {
        return exampleSentences;
    }

    public List<String> getGender() {
        return gender;
    }

    public List<String> getPluralForms() {
        return pluralForms;
    }

    public String getType() {
        return type;
    }

    public PartOfSpeech(String type, String definitions, List<String> exampleSentences, List<String> gender, List<String> pluralForms) {
        this.type = type;
        this.definitions = definitions;
        this.exampleSentences = exampleSentences;
        this.gender = gender;
        this.pluralForms = pluralForms;
    }//

    @Override
    public String toString() {
        return "PartOfSpeech{" +
                "type='" + type + '\'' +
                ", definitions='" + definitions + '\'' +
                ", exampleSentences=" + exampleSentences +
                ", gender=" + gender +
                ", pluralForms=" + pluralForms +
                '}';
    }
}
