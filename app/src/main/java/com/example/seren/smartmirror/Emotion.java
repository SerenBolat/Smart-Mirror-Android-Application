package com.example.seren.smartmirror;

public class Emotion {
    private String emotionName, date, email, emotionID;

    public Emotion() {
    }

    public Emotion(String emotionName, String date, String email, String emotionID) {

        this.emotionName = emotionName;
        this.date = date;
        this.email = email;
        this.emotionID = emotionID;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmotionID() {
        return emotionID;
    }

    public void setEmotionID(String emotionID) {
        this.emotionID = emotionID;
    }
}
