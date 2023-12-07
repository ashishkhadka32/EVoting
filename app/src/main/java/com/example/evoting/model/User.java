package com.example.evoting.model;

public class User {
    private int id;
    private String uid;
    private String username;
    private String contact;
    private String address;
    private String password;
    private String dob;
    private String gender;
    private String image;

    public User() {

    }

    public User(int id, String uid, String username, String contact, String address, String password, String dob, String gender, String image) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.contact = contact;
        this.address = address;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
