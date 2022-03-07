package com.example.covicareapp.models;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    String raspiUId, fullName, email, userId, gender, dateOfBirth, phoneNumber, countryCode, countryName;

    public UserModel() {
    }

    public UserModel(String raspiUId, String fullName, String email, String userId, String gender, String dateOfBirth, String phoneNumber, String countryCode, String countryName) {
        this.raspiUId = raspiUId;
        this.fullName = fullName;
        this.email = email;
        this.userId = userId;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getRaspiUId() { return raspiUId; }

    public void setRapsiUId(String raspiUId) { this.raspiUId = raspiUId; }

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

    public Map<String, Object> getUserData(){
        Map<String, Object> userMapData = new HashMap<>();
        userMapData.put("raspiUId", this.raspiUId);
        userMapData.put("userId", this.userId);
        userMapData.put("fullName", this.fullName);
        userMapData.put("email", this.email);
        userMapData.put("gender", this.gender);

        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.from(formatDateTime.parse(this.dateOfBirth));
        Timestamp timestamp = Timestamp.valueOf(localDate + " 00:00:00.000000000");

        userMapData.put("dateOfBirth", timestamp);
        userMapData.put("phoneNumber", this.phoneNumber);
        userMapData.put("countryCode", this.countryCode);
        userMapData.put("countryName", this.countryName);
        return userMapData;
    }

}
