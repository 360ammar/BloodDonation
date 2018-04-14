package com.example.ammar.blooddonation.Modals;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Ammar on 11/14/2017.
 */

public class UserProfile {
    private String name;
    private String email;
    private String phone;
    private boolean gender;
    private String dob;
    private String address;
    private String city;
    private String bloodgroup;
    private boolean availibility;
    private Double rating;
    DatabaseReference db;
    private String userID;

    public UserProfile(){
        super();
    }
    public UserProfile(String name, String bloodgroup, String phone, DatabaseReference db, String userID){
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.phone = phone;
        this.db = db;
        this.userID = userID;
    }
    public UserProfile(String name, String email, String phone, boolean gender, String dob, String address, String city, String bloodgroup, boolean availibility ){
        super();
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.gender=gender;
        this.dob=dob;
        this.address=address;
        this.city=city;
        this.bloodgroup=bloodgroup;
        this.availibility=availibility;
    }

    public void setRating(Double rating){        this.rating = rating;    }
    public Double getRating(){
        return rating;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }
    public void setGender(boolean gender){
        this.gender = gender;
    }
    public boolean getGender(){
        return gender;
    }
    public void setDOB(String dob){
        this.dob = dob;
    }
    public String getDOB(){
        return dob;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return city;
    }
    public void setBloodgroup(String bloodgroup){
        this.bloodgroup = bloodgroup;
    }
    public String getBloodgroup(){
        return bloodgroup;
    }
    public void setAvailibility(boolean availibility){
        this.availibility = availibility;
    }
    public boolean getAvailibility(){
        return availibility;
    }

    public void setDb(DatabaseReference db){
        this.db =db;
    }
    public DatabaseReference getDb(){
        return db;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }
    public String getUserID(){
        return userID;
    }
}
