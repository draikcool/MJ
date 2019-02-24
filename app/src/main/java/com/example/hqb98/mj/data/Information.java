package com.example.hqb98.mj.data;

import java.util.ArrayList;

public class Information {
    public int code;
    public String msg;
    public ArrayList<InfoCourse> info;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<InfoCourse> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<InfoCourse> info) {
        this.info = info;
    }
}
