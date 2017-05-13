package com.learn.uitest.Utils;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.util.UUID;

/**
 * PackageName com.learn.uitest.Utils
 * Created by uryuo on 17/5/7.
 */
public class StaticVar {
    public static String thisUUuid = "c015ea85-6701-4b7c-afae-860b39f59c8d";
    public static String connectionTar = "http://192.168.2.1:8080/";
    public static String encodingSet = "ISO-8859-1";
    public static DateTimeZone ChinaZone = DateTimeZone.forID("Asia/Shanghai");
    public static String dateTimeFormat= "yyyy年MM月dd日 HH:mm:ss EE";
    public static String fileSpacePath = connectionTar+"fileSpace/";
    public static String thisCid = "89ad59ed-755e-4b67-b7e2-73bf1cfb1293";
}
