package com.example.testone.Model;

public class SimpleQuestion {
    private int id;

    private String title;

    private String content;

    private int praise;

    private int comment;

    public int getId() { return id;}

    public void setId(int id){ this.id=id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
