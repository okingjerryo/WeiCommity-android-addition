package com.learn.uitest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.gc.materialdesign.views.ButtonRectangle;
import com.learn.uitest.Model.CommityMember;
import com.learn.uitest.R;
import com.learn.uitest.Service.DialogDoing;
import com.learn.uitest.Service.ShowDielog;
import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.StaticVar;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;
import org.json.JSONException;


@EActivity(R.layout.activity_commity_mem_manage__detail)
public class CommityMemManage_Detail extends AppCompatActivity {
    @Extra
    String CMid;

    @Extra
    int thisUtype ;

    CommityMember thisMem = new CommityMember();

    //控件
    @ViewById(R.id.CMM_tv_CType)
    TextView Utype;
    @ViewById(R.id.CMM_ev_UName)
    EditText UName;
    @ViewById(R.id.CMM_ev_UJoinTime)
    EditText UJoinTime;
    @ViewById(R.id.CMM_btn_delete)
    ButtonRectangle delete;

    @ViewById(R.id.CMM_btn_update)
    CircularProgressButton button;


    @AfterViews
    void init(){
        getMemInfo();

    }


    //初始化获取当前人员数据
    @Background
    void getMemInfo(){
        //1.封装
        String thisAPi = "api/commity/getCMem/CMid";
        //2.装Json
        HttpJson json = new HttpJson();
        CommityMember sendI = new CommityMember();
        sendI.setCMid(this.CMid);
        //3.封装
        json.setClassName("CommityMember:CommityMem-getOne");
        json.setClassObject(sendI);
        json.constractJsonString();
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        getOneResult(re);
    }

    @UiThread
    void getOneResult(HttpJson re){
        try{
            if (re.getStatusCode()!=400)
                throw new IllegalAccessException();
            re.resolveJsonObjectString(CommityMember.class);
            //重封装时间
            String UJoinTime = re.getPara("UJoinTime");


            thisMem = new CommityMember((CommityMember) re.getClassObject());
            thisMem.setUJoinTime(new DateTime(UJoinTime));
            Utype.setText(thisMem.getUTypeName());
            UName.setText(thisMem.getUNackName());
            this.UJoinTime.setText(thisMem.getUJoinTime().toString(StaticVar.dateTimeFormat));

            if (thisMem.getUUuid().equals(StaticVar.thisUUuid))
                delete.setEnabled(false);
        }catch (IllegalAccessException e){
            ShowDielog.showAlertNormal(this,"获取出错",re.getMessage());
        }catch (Exception e){
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }
    }

    //提交修改操作
    @Click
    void CMM_btn_update(){
        setProgressBar(0);
        update();
    }
    @Background
    void update(){
        setProgressBar(10);
        //1.封装
        String thisAPi = "api/commity/usertype/edit";
        //2.装Json
        HttpJson json = new HttpJson();
        //3.封装
        setProgressBar(30);
        json.setClassName("CommityMember:CommityMember-set");
        try {
            json.setPara("UJoinTime",thisMem.getUJoinTime().toString());
            json.setPara("thisUUid",StaticVar.thisUUuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json.setClassObject(thisMem);
        json.constractJsonString();
        setProgressBar(50);
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        setProgressBar(80);
        getUResult(re);
    }
    @UiThread
    void getUResult(HttpJson re){
        setProgressBar(90);
        try {
            if (re.getStatusCode()!=400)
                throw new IllegalAccessException();
            setProgressBar(100);

        }catch (IllegalAccessException e){
            setProgressBar(-1);
            ShowDielog.showAlertNormal(this,"提交出错",re.getMessage());
        }catch (Exception e){
            setProgressBar(-1);
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }
    }

    //删除成员操作
    @Background
    void deleteUser(){
        //1.封装
        String thisAPi = "api/commity/delCommityUser";
        //2.装Json
        HttpJson json = new HttpJson();
        //3.封装
        json.setClassName("CommityMember:CommityMember-kick");
        try {
            json.setPara("UUuid",StaticVar.thisUUuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        json.setClassObject(thisMem);
        json.constractJsonString();
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        getDResult(re);

    }

    @UiThread
    void getDResult(HttpJson re){
        try {
            if (re.getStatusCode()!=400)
                throw new IllegalAccessException();

            CommityMemManage_.intent(this).Cid(StaticVar.thisCid).start();
        }catch (IllegalAccessException e){
            setProgressBar(-1);
            ShowDielog.showAlertNormal(this,"提交出错",re.getMessage());
        }catch (Exception e){
            setProgressBar(-1);
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }
    }
    @Click
    void CMM_tv_CType(){
        setProgressBar(0);

        if (thisUtype==4) {
            final String[] menu = {"小黑屋", "正式成员", "管理员", "副社长","社长"};
            ShowDielog.showChooseButtomDig(this, menu, Utype, new DialogDoing() {
                @Override
                public void doInDiaLog(int positon) {
                    if (positon == 0) {
                        thisMem.setUtype(positon - 1);
                    } else
                        thisMem.setUtype(positon);
                    Utype.setText(menu[positon]);
                }
            });
        }else {
            final String[] menu = {"小黑屋", "正式成员", "管理员"};
            ShowDielog.showChooseButtomDig(this, menu, Utype, new DialogDoing() {
                @Override
                public void doInDiaLog(int positon) {
                    if (positon == 0) {
                        thisMem.setUtype(positon - 1);
                    } else
                        thisMem.setUtype(positon);
                    Utype.setText(menu[positon]);
                }
            });
        }
    }

    @Click
    void CMM_btn_delete(){
        final String[] menu = {"确定踢出"};
        ShowDielog.showTitleChooseButtomDig(this, "请注意，此操作无法撤销。\n您确定要这么做吗？",
                menu, delete, new DialogDoing() {
                    @Override
                    public void doInDiaLog(int positon) {
                        deleteUser();
                    }
                });
    }

    //更新Progress Bar
    @UiThread
    void setProgressBar(int i){
        button.setProgress(i);
    }

}
