package com.learn.uitest.Model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;


/**
 * Created by uryuo on 17/4/22.
 */

public class UserExtend {
    private String UUuid;
    private String UNackName;
    private String USex;
    private DateTime UBirthday;
    private String UTag;
    private String USign;
    private String UHeadImg;

    public DateTime getUBirthday() {
        return UBirthday;
    }

    public void setUBirthday(DateTime UBirthday) {
        this.UBirthday = UBirthday;
    }

    public String getUUuid() {
        return UUuid;
    }

    public void setUUuid(String UUuid) {
        this.UUuid = UUuid;
    }

    public String getUNackName() {
        return UNackName;
    }

    public void setUNackName(String UNackName) {
        this.UNackName = UNackName;
    }

    public String getUSex() {
        return USex;
    }

    public void setUSex(String USex) {
        this.USex = USex;
    }



    public String getUTag() {
        return UTag;
    }

    public void setUTag(String UTag) {
        this.UTag = UTag;
    }

    public String getUSign() {
        return USign;
    }

    public void setUSign(String USign) {
        this.USign = USign;
    }

    public String getUHeadImg() {
        return UHeadImg;
    }

    public void setUHeadImg(String UHeadImg) {
        this.UHeadImg = UHeadImg;
    }

}
