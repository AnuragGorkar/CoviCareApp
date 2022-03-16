package com.example.covicareapp.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlineUserModel {
    String raspiUId, fullName, email, userId, gender, phoneNumber, countryCode, countryName;
    com.google.firebase.Timestamp dateOfBirth;
    ArrayList<String> groupsAddedTo, groupsCreated;

    public OnlineUserModel() {
    }

    public OnlineUserModel(String raspiUId, String fullName, String email, String userId, String gender, com.google.firebase.Timestamp dateOfBirth, String phoneNumber, String countryCode, String countryName, ArrayList<String> groupsAddedTo, ArrayList<String> groupsCreated) {
        this.raspiUId = raspiUId;
        this.fullName = fullName;
        this.email = email;
        this.userId = userId;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.groupsAddedTo = groupsAddedTo;
        this.groupsCreated = groupsCreated;
    }

    @Override
    public String toString() {
        return "OnlineUserModel{" +
                "raspiUId='" + raspiUId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", groupsAddedTo=" + groupsAddedTo +
                ", groupsCreated=" + groupsCreated +
                '}';
    }

    public ArrayList<String> getGroupsAddedTo() {
        return groupsAddedTo;
    }

    public void setGroupsAddedTo(ArrayList<String> groupsAddedTo) {
        this.groupsAddedTo = groupsAddedTo;
    }

    public ArrayList<String> getGroupsCreated() {
        return groupsCreated;
    }

    public void setGroupsCreated(ArrayList<String> groupsCreated) {
        this.groupsCreated = groupsCreated;
    }

    public String getRaspiUId() {
        return raspiUId;
    }

    public void setRaspiUId(String raspiUId) {
        this.raspiUId = raspiUId;
    }

    public void setRapsiUId(String raspiUId) {
        this.raspiUId = raspiUId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public com.google.firebase.Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(com.google.firebase.Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Map<String, Object> getUserData() {
        Map<String, Object> userMapData = new HashMap<>();
        userMapData.put("raspiUId", this.raspiUId);
        userMapData.put("userId", this.userId);
        userMapData.put("fullName", this.fullName);
        userMapData.put("email", this.email);
        userMapData.put("gender", this.gender);

//        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Log.i("User DOB Format at date formatter :", this.dateOfBirth.toString());
//        LocalDate localDate = LocalDate.from(formatDateTime.parse(this.dateOfBirth.toString()));
//        Timestamp timestamp = Timestamp.valueOf(localDate + ".000000000");

//        DateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = (Date)formatter.parse(str_date);
//
//
//        Log.i("User DOB Format at date formatter :", this.dateOfBirth.toString());
//        Timestamp timestamp = new ;
//
//        Date date = new Date(this.dateOfBirth.toString())
//
//        try {
//            timestamp = Timestamp.valueOf(formatDateTime.parse(this.dateOfBirth.toString()) + ".000000000");
//        } catch (ParseException e) {
//            Log.i("Exception", e.getMessage().toString());
//            timestamp = Timestamp.valueOf(this.dateOfBirth.toString()  + ".000000000");
//
//            e.printStackTrace();
//        }


        userMapData.put("dateOfBirth", this.dateOfBirth);
        userMapData.put("phoneNumber", this.phoneNumber);
        userMapData.put("countryCode", this.countryCode);
        userMapData.put("countryName", this.countryName);
        userMapData.put("groupsAddedTo", this.groupsAddedTo);
        userMapData.put("groupsCreated", this.groupsCreated);
        return userMapData;
    }

}
