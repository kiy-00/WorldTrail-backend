package com.ruoyi.word.entity.words;
public class Phonetics {
    private String ipa;
    private String audio;
    public Phonetics(String ipa, String audio) {
        this.ipa = ipa;
        this.audio = audio;
    }
    public String getIpa() {
        return ipa;
    }

    public String getAudio() {
        return audio;
    }
    @Override
    public String toString() {
        return "Phonetics{" +
                "ipa='" + ipa + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }
}
