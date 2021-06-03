package com.aurelionsulll.i_learn.models;

public class Post {

    private String id;
    private String title;
    private String description;
    private String user_id;
    private String image;
    private User user;

    public Post(String id, String title, String description, String user_id, String image, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.image = image;
        this.user = user;
    }

    public Post() {
    }



    public Post(String title, String description, String user_id, String image) {
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
