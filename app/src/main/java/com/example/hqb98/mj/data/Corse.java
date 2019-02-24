package com.example.hqb98.mj.data;

import org.litepal.crud.LitePalSupport;

public class Corse extends LitePalSupport{
    private int id;
    private String corseName;
    private String corseNumber;
    private String classRoom;
    private String corseWeek;
    private String teacherName;
    private int section;
    private int weekDay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public String getCorseName() {
        return corseName;
    }

    public void setCorseName(String corseName) {
        this.corseName = corseName;
    }

    public String getCorseNumber() {
        return corseNumber;
    }

    public void setCorseNumber(String corseNumber) {
        this.corseNumber = corseNumber;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getCorseWeek() {
        return corseWeek;
    }

    public void setCorseWeek(String corseWeek) {
        this.corseWeek = corseWeek;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public String toString() {
        String s = corseNumber+" "+corseName+" "+teacherName+" "+corseWeek+" "+classRoom+" "+section+" "+weekDay;
        return s;
    }
}
