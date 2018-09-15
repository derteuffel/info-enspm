package com.derteuffel.infoenspm.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Post {


    private int id;
    private String titre, description;
    private Date date;

    private List<String> pieceJointes;
    private String userName;
    private String avatar;

    public Post() {
    }

    public Post(String titre, String description, Date date) {
        this.titre = titre;
        this.description = description;
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPieceJointes() {
        return pieceJointes;
    }

    public void setPieceJointes(List<String> pieceJointes) {
        this.pieceJointes = pieceJointes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", pieceJointes=" + pieceJointes +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
