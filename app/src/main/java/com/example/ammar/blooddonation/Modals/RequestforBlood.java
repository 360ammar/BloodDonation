package com.example.ammar.blooddonation.Modals;

import java.util.Map;

/**
 * Created by Ammar on 11/14/2017.
 */

public class RequestforBlood {
    private String bloodgroup;
    private Map daterequested;
    private String userID;
    private Double Lat;
    private Double Lon;
    private int units;
    private String levelOfUrgency;
    private String location;
    private String condition;
    private Map dateSchedule;
    private String phone;
    private String name;
    private Double rating;

    public RequestforBlood(){
        super();
    }
    public RequestforBlood(String bloodgroup, Map daterequested, String userID){
        super();
        this.bloodgroup=bloodgroup;
        this.daterequested=daterequested;
        this.userID=userID;
    }

    public void setBloodGroup(String bloodgroup){
        this.bloodgroup = bloodgroup;
    }
    public String getBloodGroup(){
        return bloodgroup;
    }
    public void setDaterequested(Map daterequested){
        this.daterequested = daterequested;
    }
    public Map getDaterequested(){
        return daterequested;
    }
    public void setUserID(String userID){        this.userID = userID;    }
    public String getUserID(){
        return userID;
    }

    public void setLat(Double Lat){        this.Lat = Lat;    }
    public Double getLat(){
        return Lat;
    }

    public void setRating(Double rating){        this.rating = rating;    }
    public Double getRating(){
        return rating;
    }

    public void setLon(Double Lon){        this.Lon = Lon;    }
    public Double getLon(){
        return Lon;
    }

    public void setUnits(int units){        this.units = units;    }
    public int getUnits(){
        return units;
    }



    public void setLevelOfUrgency(String levelOfUrgency){        this.levelOfUrgency  =  levelOfUrgency;    }
    public  String LevelOfUrgency(){
        return  levelOfUrgency;
    }
    public void setLocation(String location){        this.location  = location;    }
    public String getLocation(){
        return  location;
    }
    public void setCondition(String condition){        this.condition = condition;    }
    public String getCondition(){
        return  condition;
    }
    public void setDateSchedule(Map dateSchedule){        this.dateSchedule  = dateSchedule;    }
    public Map getDateSchedule(){
        return  dateSchedule;
    }
    public void setPhone(String phone){        this.phone  = phone;    }
    public String  getPhone(){
        return  phone;
    }
    public void setName(String name){this.name  = name;    }
    public String getName(){
        return name;
    }




}
