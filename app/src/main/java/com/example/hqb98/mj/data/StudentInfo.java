package com.example.hqb98.mj.data;

import com.google.gson.annotations.SerializedName;

public class StudentInfo {
    public int code;
    public String msg;
    @SerializedName("info")
    public UserInfo userInfo;

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
