package com.example.aap.bplfantasyleague.utility;

/**
 * Created by AAP on 11/10/2017.
 */

public class Converter {
    public int toInt(String s){
        return Integer.parseInt(s.substring(8,s.length()-3));
    }
    public String toString(int x){
        return Integer.toString(x)+'M';
    }
    public String toFirstName(String s){
        String[]Names = s.split(" ");
        return Names[0];
    }
}
