package com.example.seren.smartmirror;

public class ToDoList {
    private String date, content, notesID, situation, email;

    public ToDoList() {
    }

    public ToDoList(String notesID, String date, String content,  String situation) {
        this.date = date; //primary key and key
        this.content = content;
        this.notesID = notesID;
        this.situation = situation;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotesID() {
        return notesID;
    }

    public void setNotesID(String notesID) {
        this.notesID = notesID;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
