package com.aciv.parceros.Models.Database;

public class Comment {
    private int id;
    private User created_by, created_to;
    private String content;

    public Comment(int id, User created_by, User created_to, String content) {
        this.id = id;
        this.created_by = created_by;
        this.created_to = created_to;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public User getCreated_to() {
        return created_to;
    }

    public void setCreated_to(User created_to) {
        this.created_to = created_to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
