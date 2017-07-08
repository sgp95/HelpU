package com.sgp95.santiago.helpu.model;

/**
 * Created by GRLIMA on 07/07/2017.
 */

public class History {
    private String ID;
    private String Username;
    private String Comment;
    private String image;

    public History(String ID, String username, String comment, String image) {
        this.ID = ID;
        Username = username;
        Comment = comment;
        this.image = image;
    }

    public History(){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}