package com.example.tutor.finder1.Helper;

/**
 * Created by Rana on 10-03-18.
 */

public class CommentDBHelper {
    String image_commenter;
    String commenter_name;
    String posting_time;
    String comment;
    String like;
    String dislike;

    public CommentDBHelper() {
    }

    public CommentDBHelper(String image_commenter, String commenter_name, String posting_time, String comment, String like, String dislike) {
        this.image_commenter = image_commenter;
        this.commenter_name = commenter_name;
        this.posting_time = posting_time;
        this.comment = comment;
        this.like = like;
        this.dislike = dislike;
    }

    public String getImage_commenter() {
        return image_commenter;
    }

    public void setImage_commenter(String image_commenter) {
        this.image_commenter = image_commenter;
    }

    public String getCommenter_name() {
        return commenter_name;
    }

    public void setCommenter_name(String commenter_name) {
        this.commenter_name = commenter_name;
    }

    public String getPosting_time() {
        return posting_time;
    }

    public void setPosting_time(String posting_time) {
        this.posting_time = posting_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }
}
