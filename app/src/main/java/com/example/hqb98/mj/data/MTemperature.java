package com.example.hqb98.mj.data;

public class MTemperature {
    private String tTime;
    private int tImage;
    private String tTemperature;

    public MTemperature() {
    }

    public MTemperature(String tTime, int tImage, String tTemperature) {
        this.tTime = tTime;
        this.tImage = tImage;
        this.tTemperature = tTemperature;
    }

    public String gettTime() {
        return tTime;
    }

    public void settTime(String tTime) {
        this.tTime = tTime;
    }

    public int gettImage() {
        return tImage;
    }

    public void settImage(int tImage) {
        this.tImage = tImage;
    }

    public String gettTemperature() {
        return tTemperature;
    }

    public void settTemperature(String tTemperature) {
        this.tTemperature = tTemperature;
    }
}
