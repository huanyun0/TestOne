package com.example.testone.Model;

public class User {

    private String id;

    private String password;

    private String name;

    private void setId(String id){ this.id=id; }

    public String getId() {
        return id;
    }

    private void setPwd(String password){ this.password=password; }

    public String getPwd() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}