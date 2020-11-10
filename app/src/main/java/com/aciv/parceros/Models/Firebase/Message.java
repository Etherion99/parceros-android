package com.aciv.parceros.Models.Firebase;

import com.google.firebase.Timestamp;

public class Message {
    private String creator, content;
    private Timestamp date;

    public Message() {
        this.creator = "";
        this.content = "";
        this.date = null;
    }

    public Message(String creator, String content, Timestamp date) {
        this.creator = creator;
        this.content = content;
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
