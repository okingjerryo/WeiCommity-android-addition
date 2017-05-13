package com.learn.uitest.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.dd.CircularProgressButton;
import com.learn.uitest.Model.CommityInfo;
import com.learn.uitest.R;
import com.learn.uitest.Service.ShowDielog;
import com.learn.uitest.Service.showChoosePic;
import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.LoadImagesTask;
import com.learn.uitest.Utils.StaticVar;
import org.androidannotations.annotations.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

@EActivity(R.layout.activity_commity_info_manage)
public class CommityInfoManage extends AppCompatActivity {

    //需要传过来的值
    @Extra
    String Cid = StaticVar.thisCid;
    @Extra
    int UserType;

    String imagePath;
    String thisLastPath;
    showChoosePic showChoosePic = new showChoosePic(this,CommityInfoManage.this);
    //需要的类
    CommityInfo thisInfo ;

    boolean editImgFlag = false;
    //控件
    @ViewById(R.id.img_CommityHImg)
    ImageView CHeadImg;
    @ViewById(R.id.CMI_btn_update)
    CircularProgressButton updateButton;
    @ViewById(R.id.CMI_et_CCreatTime)
    EditText CCreatTime;
    @ViewById(R.id.CMI_et_CIntroduce)
    EditText Cintroduce;
    @ViewById(R.id.CMI_et_CMeMCount)
    EditText CMeMCount;
    @ViewById(R.id.CMI_et_CName)
    EditText CName;
    @ViewById(R.id.CMI_et_CNewDemand)
    EditText CNewDemand;
    @ViewById(R.id.CMI_et_CTag)
    EditText CTag;
    @ViewById(R.id.CMI_isNewIn)
    CheckBox isNewIn;
    @ViewById(R.id.CMI_btn_update)
    CircularProgressButton button;

    //初始化方法 1.获取社团信息 并注入进去
    @AfterViews
    void init(){
        getThisInfo();
    }

    //获得当前社团信息
    @Background
    void getThisInfo(){
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
            this.thisInfo = (CommityInfo) re.getClassObject();
            String CCreatTimeStr = re.getPara("CCreatTime");
            thisInfo.setCCreateTime(new DateTime(CCreatTimeStr));
            //将所有信息装入
            CCreatTime.setText(thisInfo.getCCreateTime().toString(StaticVar.dateTimeFormat));
            Cintroduce.setText(thisInfo.getCintroduce());
            CMeMCount.setText(thisInfo.getCMeMCount()+"");
            CName.setText(thisInfo.getCName());
            CNewDemand.setText(thisInfo.getCNMDemand());
            CTag.setText(thisInfo.getCTag());
            int checkState = thisInfo.getCIsNMin();
            if(checkState==1)
                isNewIn.setChecked(true);
            else
                isNewIn.setChecked(false);

            String nImgPath = StaticVar.fileSpacePath+thisInfo.getCHeadImg();
            //图片类装载
            new LoadImagesTask(CHeadImg).execute(nImgPath);

        }catch (IllegalArgumentException e){
            ShowDielog.showAlertNormal(this,"请求出错","请求不合法");
        }catch (Exception e){
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }
    }

    //编辑内容 根据不同来编辑
    @Click
    void CMI_btn_editable(){
        setProBtn(0);
        if (UserType>3){
            CName.setEnabled(true);
            editImgFlag = true;
            isNewIn.setEnabled(true);
        }
        CTag.setEnabled(true);
        Cintroduce.setEnabled(true);
        CNewDemand.setEnabled(true);
    }

    //imgbar onclick
    @Click
    void img_CommityHImg(){
        if (editImgFlag) {
            showChoosePic.setIv_personal_icon(CHeadImg);
            showChoosePic.showChoosePicDialog();
        }
    }

    //将修改上传 事件
    @Click
    void CMI_btn_update(){
        setProBtn(0);
        //全部变为不可编辑
        isNewIn.setEnabled(false);
        editImgFlag =false;
        ImageView CHeadImg;
        CircularProgressButton updateButton;
        CCreatTime.setEnabled(false);
        Cintroduce.setEnabled(false);
        CMeMCount.setEnabled(false);
        CName.setEnabled(false);
        CNewDemand.setEnabled(false);
        CTag.setEnabled(false);

        setProBtn(10);
        thisLastPath = thisInfo.getCHeadImg();
        if (imagePath==null||imagePath.isEmpty())
            imagePath = thisInfo.getCHeadImg();
        else{
            File tarPhoto = new File(imagePath);
            try {
                thisInfo.setCImgObj(FileUtils.readFileToString(tarPhoto,StaticVar.encodingSet));
                thisInfo.setCHeadImg(imagePath);
            } catch (IOException e) {
                ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
            }
        }
        setProBtn(25);
        updateToServer();
    }
    @Background
    void  updateToServer(){
        //1.封装
        String thisAPi = "api/commity/comomityInfo/edit";
        //2.装Json
        HttpJson json = new HttpJson();
        CommityInfo sendI = thisInfo;
        sendI.setCTag(CTag.getText().toString());
        sendI.setCNMDemand(CNewDemand.getText().toString());
        sendI.setCName(CName.getText().toString());
        setProBtn(40);
        if (isNewIn.isChecked())
            sendI.setCIsNMin(1);
        else
            sendI.setCIsNMin(0);

        sendI.setCintroduce(Cintroduce.getText().toString());

        //para Set
        try {
            json.setPara("UUuid",StaticVar.thisUUuid);
            json.setPara("lastPath",thisLastPath);
            json.setPara("CCreatTime",thisInfo.getCCreateTime().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setProBtn(55);
        //3.封装
        json.setClassName("CommityInfo:publish");
        json.setClassObject(sendI);
        json.constractJsonString();
        setProBtn(75);
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        setProBtn(90);
        getUpResult(re);
    }

    @UiThread
    void getUpResult(HttpJson re){
        if (re.getStatusCode()==400){
            setProBtn(100);
        }else {
            setProBtn(-1);
            ShowDielog.showAlertNormal(this,"更新出错",re.getMessage());
        }

    }
    //更新progressButton事件
    @UiThread
    void setProBtn(int i){
        button.setProgress(i);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //调用照片 以及获得保存的照片路径
        super.onActivityResult(requestCode, resultCode, data);
        imagePath = showChoosePic.onActivityResult(requestCode,resultCode,data);
    }
}
