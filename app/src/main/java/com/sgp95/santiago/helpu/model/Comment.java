package com.sgp95.santiago.helpu.model;

/**
 * Created by GRLIMA on 07/07/2017.
 */

public class Comment {
    private String CommentId;
    private String UserName;
    private String UserCode;
    private String Comment;
    private String CommentDate;
    private String image;

    public Comment(){}

    public Comment(String commentId, String username, String userCode, String comment, String commentDate, String image) {
        CommentId = commentId;
        UserName = username;
        UserCode = userCode;
        Comment = comment;
        CommentDate = commentDate;
        this.image = image;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String username) {
        UserName = username;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
