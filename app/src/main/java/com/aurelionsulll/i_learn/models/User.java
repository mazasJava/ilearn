package com.aurelionsulll.i_learn.models;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {

    private String id;
    private String name;
    private String image;

    public User() {
    }

    public User(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public User(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(int contentLayoutId, String id, String name, String image) {
        super(contentLayoutId);
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
