package com.example.covicareapp.models;

public class LocalUserModel {
    String localUserId, name, gender;
    com.google.firebase.Timestamp dateOfBirth;

    public LocalUserModel() {

    }

    public LocalUserModel(String localUserId, String name, String gender, com.google.firebase.Timestamp dateOfBirth) {
        this.localUserId = localUserId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "LocalUserModel{" +
                "localUserId='" + localUserId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public String getLocalUserId() {
        return localUserId;
    }

    public void setLocalUserId(String localUserId) {
        this.localUserId = localUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
