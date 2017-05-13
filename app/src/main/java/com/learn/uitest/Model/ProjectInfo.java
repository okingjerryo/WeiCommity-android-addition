package com.learn.uitest.Model;

import org.joda.time.DateTime;

/**
 * PackageName com.learn.uitest.Model
 * Created by uryuo on 17/5/10.
 */
public class ProjectInfo {
    private String PId;
    private String CId;
    private String CreatUUuid;
    private DateTime PEndTime;
    private String PTitle;
    private String PIntroduce;
    private DateTime PCreatTime;
    private int PState;
    private String PType; //中文记录

    public String getPId() {
        return PId;
    }

    public void setPId(String PId) {
        this.PId = PId;
    }

    public String getCId() {
        return CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCreatUUuid() {
        return CreatUUuid;
    }

    public void setCreatUUuid(String creatUUuid) {
        CreatUUuid = creatUUuid;
    }

    public DateTime getPEndTime() {
        return PEndTime;
    }

    public void setPEndTime(DateTime PEndTime) {
        this.PEndTime = PEndTime;
    }

    public String getPTitle() {
        return PTitle;
    }

    public void setPTitle(String PTitle) {
        this.PTitle = PTitle;
    }

    public String getPIntroduce() {
        return PIntroduce;
    }

    public void setPIntroduce(String PIntroduce) {
        this.PIntroduce = PIntroduce;
    }

    public DateTime getPCreatTime() {
        return PCreatTime;
    }

    public void setPCreatTime(DateTime PCreatTime) {
        this.PCreatTime = PCreatTime;
    }

    public int getPState() {
        return PState;
    }

    public void setPState(int PState) {
        this.PState = PState;
    }

    public String getPType() {
        return PType;
    }

    public void setPType(String PType) {
        this.PType = PType;
    }

    public ProjectInfo() {
        super();
    }
    //  copy Constract
    public ProjectInfo(ProjectInfo old){
        super();
        this.CId = old.getCId();
        this.CreatUUuid = old.getCId();
        this.PCreatTime = new DateTime(old.getPCreatTime().toString());
        this.PEndTime = new DateTime(old.getPEndTime().toString());
        this.PId = old.PId;
        this.PIntroduce = old.PIntroduce;
        this.PState =old.PState;
        this.PTitle = old.PTitle;
        this.PType = old.PType;
    }
}
