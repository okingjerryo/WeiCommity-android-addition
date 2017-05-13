package com.learn.uitest.Activity;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import android.widget.ImageView;
import com.dd.CircularProgressButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.learn.uitest.Model.CommityInfo;
import com.learn.uitest.R;
import com.learn.uitest.Service.ShowDielog;
import com.learn.uitest.Service.showChoosePic;
import com.learn.uitest.Utils.*;
import org.androidannotations.annotations.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.File;
import java.net.HttpURLConnection;

import java.sql.Timestamp;

@EActivity(R.layout.activity_regist_commity)
public class RegistCommityActivity extends AppCompatActivity {
    private String imagePath = null;
    private com.learn.uitest.Service.showChoosePic showChoosePic = new showChoosePic(this,RegistCommityActivity.this);
    @ViewById(R.id.CName)
    EditText CNameText;

    @ViewById(R.id.CIntroduce)
    EditText CIntroduceText;

    @ViewById(R.id.CTag)
    EditText CTagText;

    @ViewById(R.id.CNMDemand)
    EditText CNMDemandText;

    @ViewById(R.id.btn_isCNameExsit)
    CircularProgressButton isCNameEButton;

    @ViewById(R.id.btn_registCommity)
    ActionProcessButton registCommityButton;

    @ViewById(R.id.img_CommityHImg)
    ImageView CHeadImg;

    HttpURLConnection client;

    @AfterViews
    void executeOnload(){
        if (StaticVar.thisUUuid.isEmpty()) {
            FirstAnnoActivity_.intent(this).start();
            finish();
        }
    }


    //注册社团按钮事件
    @Click
    void btn_registCommity(){
        // 排除不符合条件
        if (isCNameEButton.getProgress()!=100) {
            ShowDielog.showAlertNormal(this, "无法注册", "您的社团名为空或已存在,请返回更改");
            return;
        }
        if (imagePath ==null) {
            ShowDielog.showAlertNormal(this, "无法注册", "社长。。咱社团的头像呢。。。");
            return;
        }
        //启动Bar
        registCommityButton.setMode(ActionProcessButton.Mode.ENDLESS);
        CommmityRegistSend();
    }
    //社团检查按钮事件
    @Click
    void btn_isCNameExsit(){
        setisCProgress(10);
        sender();
    }

    //相机调用的Result事件
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //调用照片 以及获得保存的照片路径
        super.onActivityResult(requestCode, resultCode, data);
        imagePath = showChoosePic.onActivityResult(requestCode,resultCode,data);
    }

    //头像点击img事件
    @Click
    void img_CommityHImg(){
        showChoosePic.setIv_personal_icon(CHeadImg);
        showChoosePic.showChoosePicDialog();
    }

    //当检测到CName框出现了变化
    @FocusChange(R.id.CName)
    void CNameFocusChange(){
        setisCProgress(0);
        if (CNameText.isFocused()||CNameText.getText().toString().isEmpty())
            return;

        setisCProgress(10);
        sender();
    }
    //检查社团名的事件封装
    void sender(){
        //1 constract tarPath
        String thisApi = "api/commity/isNameExist";
        //2. constract json
        HttpJson json = new HttpJson();
        try {
            json.setClassName("form:isCommityNameExist");
            json.setPara("CName", CNameText.getText().toString());
            json.constractJsonString();
            setisCProgress(40);
        }catch (Exception e){
           setisCProgress(-1);
            e.printStackTrace();
        }
        //3 sending
        //sendGetJson(thisApi,json.getJsonString());
        sendCheckForIsExist(StaticVar.connectionTar+thisApi,json.getJsonString());
    }

    //对于 注册事件的封装
    @Background
    void CommmityRegistSend(){
        //1 指向api
        String thisApi = "api/commity/regist/common";
        //2.封装类
        HttpJson sender = new HttpJson();
        try{
            sender.setPara("UUuid",StaticVar.thisUUuid);
            //将各项信息封装到类
            CommityInfo info = new CommityInfo();
            info.setCCreateTime(new DateTime(DateTime.now()));
            info.setCHeadImg(imagePath);
            info.setCintroduce(CIntroduceText.getText().toString());
            info.setCIsNMin(0);
            info.setCName(CNameText.getText().toString());
            info.setCNMDemand(CNMDemandText.getText().toString());
            info.setCTag(CTagText.getText().toString());
            //把图片存进来
            File resPath = new File(imagePath);
            String imgObj = FileUtils.readFileToString(resPath,StaticVar.encodingSet);
            info.setCImgObj(imgObj);

            //装载
            sender.setClassObject(info);
            sender.setClassName("CommityInfo:commityRegist-common");
            sender.setClassObject(info);
            sender.constractJsonString();
        }catch (Exception e){
            e.printStackTrace();

        }
        //3.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisApi,sender.getJsonString());
        getRegistResult(re);

    }
    //注册 获得结果
    @UiThread
    void getRegistResult(HttpJson json){
        try {
            //抛出服务器异常
            if (json.getStatusCode() != 400)
                throw  new IllegalArgumentException();

           String newCid =  json.getMessage();
           ShowDielog.showAlertNormal(this,"注册成功","恭喜~ 注册成功~  Cid："+newCid);

        }catch (IllegalArgumentException e){
            ShowDielog.showAlertNormal(this,"无法注册",json.getMessage());
        }catch (Exception e){
            ShowDielog.showAlertNormal(this,"无法注册","客户端异常,原因："+e.getMessage());
        }
    }
    //server请求数据
    @Background
    void sendCheckForIsExist(String urlString,String jsonString){
        HttpJson re = GetFromServer.execute(urlString,jsonString);
        getResult(re);
    }
    //获得结果
    @UiThread
    void getResult(HttpJson output){

        setisCProgress(75);
        try {
            if (output.getStatusCode()!= 400)
                throw new IllegalArgumentException();
            String Cid = output.getPara("Cid");
            if (Cid.equals("不存在"))
                setisCProgress(100);
            else
                setisCProgress(-1);
        }catch (IllegalArgumentException e){
            setisCProgress(-1);
        }catch (JSONException e){
            setisCProgress(-1);
        }
    }

    //两个Button在UI线程更新自己的状态

    @UiThread
    void setisCProgress(int state){
        this.isCNameEButton.setProgress(state);
    }


}
