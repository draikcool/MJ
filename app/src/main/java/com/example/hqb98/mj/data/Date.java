package com.example.hqb98.mj.data;

import org.litepal.crud.LitePalSupport;

public class Date extends LitePalSupport{
    private int id;
    private int date_image;
    private String date_id;
    private String date_type;
    private String date_title;
    private String date_content;
    private String date_time;


//    public Date(int id, int date_image, String date_type, String date_title, String date_content, String date_time) {
//        this.id = id;
//        this.date_image = date_image;
//        this.date_type = date_type;
//        this.date_title = date_title;
//        this.date_content = date_content;
//        this.date_time = date_time;
//    }


    public String getDate_id() {
        return date_id;
    }

    public void setDate_id(String date_id) {
        this.date_id = date_id;
    }

    public String getDate_title() {
        return date_title;
    }

    public void setDate_title(String date_title) {
        this.date_title = date_title;
    }

    public int getDate_image() {
        return date_image;
    }

    public void setDate_image(int date_image) {
        this.date_image = date_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_type() {
        return date_type;
    }

    public void setDate_type(String date_type) {
        this.date_type = date_type;
    }

    public String getDate_content() {
        return date_content;
    }

    public void setDate_content(String date_content) {
        this.date_content = date_content;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
