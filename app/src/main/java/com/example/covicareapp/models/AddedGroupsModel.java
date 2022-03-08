package com.example.covicareapp.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class AddedGroupsModel {
    String createdBy, groupInfo, groupName, groupId;
    Timestamp dateCreated;
    ArrayList<String> groupUsers;

    public AddedGroupsModel() {
    }

    public AddedGroupsModel(String createdBy, String groupInfo, String groupName, String groupId, Timestamp dateCreated, ArrayList<String> groupUsers) {
        this.createdBy = createdBy;
        this.groupInfo = groupInfo;
        this.groupName = groupName;
        this.groupId = groupId;
        this.dateCreated = dateCreated;
        this.groupUsers = groupUsers;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<String> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(ArrayList<String> groupUsers) {
        this.groupUsers = groupUsers;
    }
}
