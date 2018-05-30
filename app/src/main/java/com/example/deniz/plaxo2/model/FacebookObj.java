package com.example.deniz.plaxo2.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Date;

public class FacebookObj {
    private String message;
    private Date createdDate;
    private String postId;

    public FacebookObj(){

    }

    public FacebookObj(String message, String postId){
        this.postId = postId;
        this.message = message;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
