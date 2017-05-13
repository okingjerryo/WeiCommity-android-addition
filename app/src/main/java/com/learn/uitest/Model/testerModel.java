package com.learn.uitest.Model;




import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.util.Date;

/**
 * PackageName com.learn.uitest.Model
 * Created by uryuo on 17/5/4.
 */
public class testerModel{
    String message;
    int num;
    LocalDate Time;

    public LocalDate getDate() {
        return Time;
    }

   public void setDate(LocalDate date) {
        this.Time = date;
  }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}
