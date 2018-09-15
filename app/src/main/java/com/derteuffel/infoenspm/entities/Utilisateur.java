package com.derteuffel.infoenspm.entities;

public class Utilisateur {
    private int id;
    private String avatar,nom;

    public Utilisateur() {
    }

    public Utilisateur(String avatar, String nom) {
        this.avatar = avatar;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
