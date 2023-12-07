package com.example.evoting.model;

public class Candidate {
    private int id;
    private String name;
    private String address;
    private String contact;
    private String party;
    private String nominees;
    private String image;

   public Candidate(){

   }

    public Candidate(int id, String name, String address, String contact, String party, String nominees, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.party = party;
        this.nominees = nominees;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
