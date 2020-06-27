package com.example.testone.Model;

public class Responses<T> {

    private String msg;

    private int code;

    private T object;

    private String note;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getObject() {
        return object;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
