package com.learn.uitest.Model;

import org.joda.time.DateTime;

/**
 * PackageName com.learn.uitest.Model
 * Created by uryuo on 17/5/10.
 */
public class ProjectDynamic {
    private String PDId;
    private DateTime Time;
    private String Word;
    private String PWId;
    private String PFId;
    private String PDType;

    public String getPDId() {
        return PDId;
    }

    public void setPDId(String PDId) {
        this.PDId = PDId;
    }

    public DateTime getTime() {
        return Time;
    }

    public void setTime(DateTime time) {
        Time = time;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getPWId() {
        return PWId;
    }

    public void setPWId(String PWId) {
        this.PWId = PWId;
    }

    public String getPFId() {
        return PFId;
    }

    public void setPFId(String PFId) {
        this.PFId = PFId;
    }

    public String getPDType() {
        return PDType;
    }

    public void setPDType(String PDType) {
        this.PDType = PDType;
    }
}
