package com.example.evoting.model;

public class Election {
    private int id;
    private String election;
    private String startDate;
    private String endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Election(){

    }
    public Election(int id, String election, String startDate, String endDate) {
        this.id = id;
        this.election = election;
        this.startDate = startDate;
        this.endDate = endDate;
    }



}
