package com.aurelionsulll.i_learn.models;

public class Joined {
    private String postId,userId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Joined(String postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
    public Joined() {
    }
}
