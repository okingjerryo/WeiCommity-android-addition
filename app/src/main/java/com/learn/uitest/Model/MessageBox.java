package com.learn.uitest.Model;


import org.joda.time.DateTime;

import java.sql.Timestamp;

/***
 * PackageName com.weiCommity.Model
 * 软件信箱类
 * 附加的附件类为 MessageAttach
 * Created by uryuo on 17/5/1.
 */

public class MessageBox {
    private String MId;
    private DateTime MCreateTime;
    private String MSenderId;
    private String MTarId;
    private String MTitle;
    private String MThings;
    private int MIsReaded = 0;
    private int MCheck = -2;  //-2 没有需要查看的 1同意 -1不同意  0 未查看
    private String MSpcId;

    private int MSenderType = 1;

    public int getMSenderType() {
        return MSenderType;
    }

    public void setMSenderType(int MSenderType) {
        this.MSenderType = MSenderType;
    }

    public String getMId() {
        return MId;
    }

    public void setMId(String MId) {
        this.MId = MId;
    }

    public DateTime getMCreateTime() {
        return MCreateTime;
    }

    public void setMCreateTime(DateTime MCreateTime) {
        this.MCreateTime = MCreateTime;
    }

    public String getMSenderId() {
        return MSenderId;
    }

    public void setMSenderId(String MSenderId) {
        this.MSenderId = MSenderId;
    }

    public String getMTarId() {
        return MTarId;
    }

    public void setMTarId(String MTarId) {
        this.MTarId = MTarId;
    }

    public String getMTitle() {
        return MTitle;
    }

    public void setMTitle(String MTitle) {
        this.MTitle = MTitle;
    }

    public String getMThings() {
        return MThings;
    }

    public void setMThings(String MThings) {
        this.MThings = MThings;
    }

    public int getMIsReaded() {
        return MIsReaded;
    }

    public void setMIsReaded(int MIsReaded) {
        this.MIsReaded = MIsReaded;
    }

    public String getMSpcId() {
        return MSpcId;
    }

    public void setMSpcId(String MSpcId) {
        this.MSpcId = MSpcId;
    }

    public int getMCheck() {
        return MCheck;
    }

    public void setMCheck(int MCheck) {
        this.MCheck = MCheck;
    }
}
