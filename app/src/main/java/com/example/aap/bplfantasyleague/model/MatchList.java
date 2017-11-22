package com.example.aap.bplfantasyleague.model;

public class MatchList {
    private int Id;
    private String Date;
    private String Team1;
    private String Team2;
    private String Time;
    private String Venue;
    public MatchList(){

    }

    public MatchList(int Id, String Date,String Team1,String Team2,String Time,String Venue) {
        this.Id = Id;
        this.Date = Date;
        this.Team1 = Team1;
        this.Team2 = Team2;
        this.Time = Time;
        this.Venue = Venue;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTeam1() {
        return Team1;
    }

    public void setTeam1(String team1) {
        Team1 = team1;
    }

    public String getTeam2() {
        return Team2;
    }

    public void setTeam2(String team2) {
        Team2 = team2;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public int getId() {
        return Id;
    }

    public String getDate() {
        return Date;
    }
}
