package com.example.covicareapp.models;

import java.sql.Timestamp;

public class OfflineUserModel {
    String localUserId, name, gender;
    Timestamp dateOfBirth;

    public OfflineUserModel() {

    }

    public OfflineUserModel(String localUserId, String name, String gender, Timestamp dateOfBirth) {
        this.localUserId = localUserId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "OfflineUserModel{" +
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

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
