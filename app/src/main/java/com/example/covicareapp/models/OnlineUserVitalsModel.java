package com.example.covicareapp.models;

import java.sql.Timestamp;

public class OnlineUserVitalsModel {
    double hbRating, o2Saturation, bodyTemp;
    int coughAnalysis;
    String raspiId, raspiUId, userId, profileId, analysisResult;
    Timestamp recDateTime;

    public OnlineUserVitalsModel() {
    }

    public OnlineUserVitalsModel(String userId, String raspiUId, String raspiId, String profileId, double hbRating, double o2Saturation, double bodyTemp, int coughAnalysis, Timestamp recDateTime, String analysisResult) {
        this.raspiId = raspiId;
        this.raspiUId = raspiUId;
        this.userId = userId;
        this.profileId = profileId;
        this.hbRating = hbRating;
        this.o2Saturation = o2Saturation;
        this.bodyTemp = bodyTemp;
        this.coughAnalysis = coughAnalysis;
        this.recDateTime = recDateTime;
        this.analysisResult = analysisResult;
    }

    @Override
    public String toString() {
        return "OnlineUserVitalsModel{" +
                ", userId='" + userId + '\'' +
                ", raspiUId='" + raspiUId + '\'' +
                ", raspiId='" + raspiId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", hbRating=" + hbRating +
                ", o2Saturation=" + o2Saturation +
                ", bodyTemp=" + bodyTemp +
                ", coughAnalysis=" + coughAnalysis +
                ", recDateTime=" + recDateTime +
                ", analysisResult='" + analysisResult + '\'' +
                '}';
    }

    public double getHbRating() {
        return hbRating;
    }

    public void setHbRating(double hbRating) {
        this.hbRating = hbRating;
    }

    public double getO2Saturation() {
        return o2Saturation;
    }

    public void setO2Saturation(double o2Saturation) {
        this.o2Saturation = o2Saturation;
    }

    public double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public int getCoughAnalysis() {
        return coughAnalysis;
    }

    public void setCoughAnalysis(int coughAnalysis) {
        this.coughAnalysis = coughAnalysis;
    }

    public String getRaspiId() {
        return raspiId;
    }

    public void setRaspiId(String raspiId) {
        this.raspiId = raspiId;
    }

    public String getRaspiUId() {
        return raspiUId;
    }

    public void setRaspiUId(String raspiUId) {
        this.raspiUId = raspiUId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getRecDateTime() {
        return recDateTime;
    }

    public void setRecDateTime(Timestamp recDateTime) {
        this.recDateTime = recDateTime;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
