package com.ruoyi.word.entity.words;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "words")
public class Word {
    @Id
    private ObjectId id;
    private String word;
    private String language;
    private List<PartOfSpeech> partOfSpeechList;
    private List<Phonetics> phonetics;

    public String getId() {
        return id.toHexString();
    }

    public String getWord() {
        return word;
    }


    public String getLanguage() {
        return language;
    }

    public List<PartOfSpeech> getPartOfSpeechList() {
        return partOfSpeechList;
    }

    public List<Phonetics> getPhonetics() {
        return phonetics;
    }

    public Word(String word, String language, List<Phonetics> phonetics, List<PartOfSpeech> partOfSpeechList) {
        this.word = word.toLowerCase();
        this.partOfSpeechList = partOfSpeechList;
        this.phonetics =phonetics;
        this.language = language;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", language='" + language + '\'' +
                ", partOfSpeechList=" + partOfSpeechList +
                ", phonetics=" + phonetics +
                '}';
    }
}
