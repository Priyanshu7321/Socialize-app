package com.example.socialize;

import android.net.Uri;

import java.net.URL;

public class userClass {
    String user;
    Uri url;
    String name;
    String msg="online";
    String date="1Jan";

    public userClass(){

    }
    public userClass(String name,String msg,String date,Uri url){
        this.name=name;
        this.msg=msg;
        this.date=date;
        this.url=url;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
