package com.learn.uitest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.learn.uitest.Model.CommityInfo;
import com.learn.uitest.R;
import com.learn.uitest.Service.ShowDielog;
import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.StaticVar;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;
import org.json.JSONException;

@EActivity(R.layout.activity_commity_note_manage)
public class CommityNoteManage extends AppCompatActivity {

    @Extra
    String Cid = StaticVar.thisCid;
    //获得当前社团的信息
    CommityInfo thisCInfo;

    @ViewById(R.id.CMN_et_CName)
    EditText CName;
    @ViewById(R.id.CMN_et_CNCreatTime)
    EditText CNCreatTime;
    @ViewById(R.id.CMN_et_CNNotice)
    EditText CNotice;

    @ViewById(R.id.CMN_btn_update)
    CircularProgressButton button;


    @Click
    void CMN_btn_editable(){
        CNCreatTime.setText(DateTime.now().toString("yyyy年MM月dd日 HH:mm:ss EE"));
        CNotice.setEnabled(true);
    }
    @AfterViews
    void loadthisNote(){
        getthisCommityInfo();
    }
    @Click
    void CMN_btn_update(){
        setBtnProgress(5);
        thisCInfo.setCNotice(CNotice.getText().toString());
        thisCInfo.setCNoteCTime(new DateTime(DateTime.now()));
        CNotice.setEnabled(false);
        setBtnProgress(10);

            updateNotice();

    }

    @Background
    void updateNotice()  {
        setBtnProgress(20);
        //1.封装
        String thisAPi = "api/commity/notice/publish";
        //2.装Json
        HttpJson json = new HttpJson();

        try {
            json.setPara("UUuid",StaticVar.thisUUuid);
            json.setPara("CNCTime",CNCreatTime.getText().toString());
        } catch (JSONException e) {
           ShowDielog.showAlertNormal(this,"修改出错",e.getMessage());
           setBtnProgress(-1);
        }
        setBtnProgress(40);
        //3.封装
        json.setClassName("CommityInfo:notice-publish");
        try {
            json.setPara("CNCTime",thisCInfo.getCNoteCTime().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json.setClassObject(thisCInfo);
        json.constractJsonString();
        setBtnProgress(50);
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        setBtnProgress(75);
        updateRe(re);

    }

    @UiThread
    void updateRe(HttpJson re){
        setBtnProgress(80);
        if (re.getStatusCode()!=400) {
            try {
                throw new IllegalAccessException();
            } catch (IllegalAccessException e) {
                setBtnProgress(-1);
                ShowDielog.showAlertNormal(this,"发布出现问题","不嗯发emoji哦~");

            }
        }else
            setBtnProgress(100);

    }
    @TextChange
    void CMN_et_CNNotice(){
        setBtnProgress(0);
    }

    @Background
     void getthisCommityInfo(){
        //1.封装
        String thisAPi = "api/commity/commityInfo/get";
        //2.装Json
        HttpJson json = new HttpJson();
        CommityInfo sendI = new CommityInfo();
        sendI.setCid(this.Cid);
        //3.封装
        json.setClassName("Commityinfo:CommityInfo-get");
        json.setClassObject(sendI);
        json.constractJsonString();
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        getInfoResult(re);
    }

    @UiThread
    void getInfoResult(HttpJson re){
        try {
            if (re.getStatusCode() != 400)
                throw new IllegalArgumentException(re.getMessage());

            re.resolveJsonObjectString(CommityInfo.class);
            this.thisCInfo = (CommityInfo) re.getClassObject();
            CName.setText(thisCInfo.getCName());
            String thisCNTime = re.getPara("CNCreatTime");
            if (!thisCNTime.isEmpty()) {
                thisCInfo.setCNoteCTime(new DateTime(thisCNTime));
                CNCreatTime.setText(thisCInfo.getCNoteCTime().toString(StaticVar.dateTimeFormat));
            }else{
                thisCInfo.setCNoteCTime(DateTime.now());
                CNCreatTime.setText(thisCInfo.getCNoteCTime().toString("yyyy年MM月dd日 HH:mm:ss EE"));
            }
            if (thisCInfo.getCNotice()!=null)
                CNotice.setText(thisCInfo.getCNotice());
        }catch (IllegalArgumentException e){
            ShowDielog.showAlertNormal(this,"请求出错","请求不合法");
        }catch (Exception e){
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }
    }

    @UiThread
    void setBtnProgress(int i){
        button.setProgress(i);
    }

}
