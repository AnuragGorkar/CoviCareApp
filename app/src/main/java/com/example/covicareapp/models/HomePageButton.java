package com.example.covicareapp.models;

public class HomePageButton {
    String buttonName;
    int imageId;
    int buttonId;

    public HomePageButton(String buttonName, int imageId, int buttonId) {
        this.buttonName = buttonName;
        this.imageId = imageId;
        this.buttonId = buttonId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }
}
