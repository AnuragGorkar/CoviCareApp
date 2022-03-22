package com.example.covicareapp.models;

public class OnlineUserVitalsModel {
    double hbRating, o2Saturation, bodyTemp;
    int coughAnalysis;
    String raspiId, raspiUId, userId, groupId, analysisResult;
    Long recDateTime;

    public OnlineUserVitalsModel() {
    }

    public OnlineUserVitalsModel(String userId, String raspiUId, String raspiId, String groupId, double hbRating, double o2Saturation, double bodyTemp, int coughAnalysis, Long recDateTime, String analysisResult) {
        this.raspiId = raspiId;
        this.raspiUId = raspiUId;
        this.userId = userId;
        this.groupId = groupId;
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
                ", groupId='" + groupId + '\'' +
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

    public Long getRecDateTime() {
        return recDateTime;
    }

    public void setRecDateTime(Long recDateTime) {
        this.recDateTime = recDateTime;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getgroupId() {
        return groupId;
    }

    public void setgroupId(String groupId) {
        this.groupId = groupId;
    }
}
