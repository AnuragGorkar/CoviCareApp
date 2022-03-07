package com.example.covicareapp.models;

import java.sql.Timestamp;

public class OfflineUserVitalsModel {
    double hbRating, o2Saturation, bodyTemp;
    int coughAnalysis;
    String raspiId, userId, analysisResult;
    Timestamp recDateTime;

    public OfflineUserVitalsModel() {

    }

    public OfflineUserVitalsModel(double hbRating, double o2Saturation, double bodyTemp, int coughAnalysis, String raspiId, String userId, String analysisResult, Timestamp recDateTime) {
        this.hbRating = hbRating;
        this.o2Saturation = o2Saturation;
        this.bodyTemp = bodyTemp;
        this.coughAnalysis = coughAnalysis;
        this.raspiId = raspiId;
        this.userId = userId;
        this.analysisResult = analysisResult;
        this.recDateTime = recDateTime;
    }

    @Override
    public String toString() {
        return "OfflineUserVitalsModel{" +
                "hbRating=" + hbRating +
                ", o2Saturation=" + o2Saturation +
                ", bodyTemp=" + bodyTemp +
                ", coughAnalysis=" + coughAnalysis +
                ", raspiId='" + raspiId + '\'' +
                ", userId='" + userId + '\'' +
                ", analysisResult='" + analysisResult + '\'' +
                ", recDateTime=" + recDateTime +
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public Timestamp getRecDateTime() {
        return recDateTime;
    }

    public void setRecDateTime(Timestamp recDateTime) {
        this.recDateTime = recDateTime;
    }
}
