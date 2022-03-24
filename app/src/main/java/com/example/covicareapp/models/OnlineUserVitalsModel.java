package com.example.covicareapp.models;

public class OnlineUserVitalsModel {
    double pulse, sp02, temperature;
    int coughAnalysis;
    String raspiId, raspiUId, userId, groupId, analysisResult;
    Long recDateTime;

    public OnlineUserVitalsModel() {
    }

    public OnlineUserVitalsModel(String userId, String raspiUId, String raspiId, String groupId, double pulse, double sp02, double temperature, int coughAnalysis, Long recDateTime, String analysisResult) {
        this.raspiId = raspiId;
        this.raspiUId = raspiUId;
        this.userId = userId;
        this.groupId = groupId;
        this.pulse = pulse;
        this.sp02 = sp02;
        this.temperature = temperature;
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
                ", hbRating=" + pulse +
                ", o2Saturation=" + sp02 +
                ", bodyTemp=" + temperature +
                ", coughAnalysis=" + coughAnalysis +
                ", recDateTime=" + recDateTime +
                ", analysisResult='" + analysisResult + '\'' +
                '}';
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public double getPulse() {
        return pulse;
    }

    public void setPulse(double pulse) {
        this.pulse = pulse;
    }

    public double getSp02() {
        return sp02;
    }

    public void setSp02(double sp02) {
        this.sp02 = sp02;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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
}
