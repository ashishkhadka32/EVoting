package com.example.evoting.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VicePresident {
    private int id;
    private String name;
    private String party;
    private String nominees;
    private int voteCount;
    private String image;

    public VicePresident()
    {

    }
    public VicePresident(int id, String name, String party, String nominees, int voteCount, String image) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.nominees = nominees;
        this.voteCount = voteCount;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getNominees() {
        return nominees;
    }

    public void setNominees(String nominees) {
        this.nominees = nominees;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
