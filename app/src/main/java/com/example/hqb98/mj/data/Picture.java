package com.example.hqb98.mj.data;

import org.litepal.crud.LitePalSupport;

public class Picture extends LitePalSupport {
    private int id;
    private String time;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
