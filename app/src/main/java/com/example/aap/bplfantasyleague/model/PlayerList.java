package com.example.aap.bplfantasyleague.model;

/**
 * Created by AAP on 11/8/2017.
 */

public class PlayerList {
    private String Name;
    private long Price;
    private int PlayerId;
    private String Age;
    private String Role;
    private String Country;

    public PlayerList(String name, long price, int playerId, String age, String role, String country, String team) {
        Name = name;
        Price = price;
        PlayerId = playerId;
        Age = age;
        Role = role;
        Country = country;
        Team = team;
    }

    public String getAge() {

        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }

    private String Team;

    public int getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(int playerId) {
        PlayerId = playerId;
    }

    public PlayerList(){

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getPrice() {
        return Price;
    }

    public void setPrice(long price) {
        Price = price;
    }
}
