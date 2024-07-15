package com.example.socialize;

import android.net.Uri;

public class postContentClass {
    public String userName,postQuestion;
    public String profileUri,contentUri;
    public postContentClass(String userName,String postQuestion,String profileUri,String contentUri){
        this.userName=userName;
        this.postQuestion=postQuestion;
        this.profileUri=profileUri;
        this.contentUri=contentUri;
    }
}
