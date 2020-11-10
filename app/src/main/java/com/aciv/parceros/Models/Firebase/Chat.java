package com.aciv.parceros.Models.Firebase;

import java.util.List;

public class Chat {
    private String id;
    private Message last;

    public Chat(String id) {
        this.id = id;
        this.last = new Message();
    }

    public Chat(String id, Message last) {
        this.id = id;
        this.last = last;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getLast() {
        return last;
    }

    public void setLast(Message last) {
        this.last = last;
    }
}
