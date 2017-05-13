package com.learn.uitest.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.learn.uitest.Model.CommityMember;

import com.learn.uitest.R;

import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.StaticVar;
import org.androidannotations.annotations.*;
import org.json.JSONException;

import java.util.ArrayList;

@EActivity(R.layout.activity_commity_manager)
public class CommityManager extends AppCompatActivity implements ObservableScrollViewCallbacks {


    @Extra
    String Cid = StaticVar.thisCid;

    CommityMember thisUser;
    @ViewById(R.id.CMShow_list)
    ObservableListView listView;

    @AfterViews
    void init(){
        listView.setScrollViewCallbacks(this);
        //权限管理
        checkUserType();
    }

    @Background
    void checkUserType(){
        //1.封装
        String thisAPi = "api/commity/commityMember/get";
        //2.装Json
        HttpJson json = new HttpJson();
        try {
            json.setPara("UUuid",StaticVar.thisUUuid);
            json.setPara("Cid",this.Cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //3.封装
        json.setClassName("form:getOneCommityMem");
        json.constractJsonString();
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        getMem(re);
    }

    @UiThread
    void getMem(HttpJson re){
        try {
            if (re.getStatusCode() != 400)
                throw new IllegalArgumentException();

            re.resolveJsonObjectString(CommityMember.class);
            this.thisUser = (CommityMember) re.getClassObject();
            checkAndSetView();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void checkAndSetView(){
        ArrayList<String> items = new ArrayList<String>();
        if (thisUser.getUtype()>1) {
            items.add("社团公告管理");
            items.add("社团活动管理");
            items.add("社团信息管理");
        }
        if (thisUser.getUtype()>2){

            items.add("成员权限管理");
        }
        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, items));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String thisitem = (String) listView.getItemAtPosition(position);
                menuJump(thisitem);
            }
        });
    }
    void menuJump(String selItem){
        if (selItem.equals("社团公告管理"))
            CommityNoteManage_.intent(this).Cid(this.Cid).start();
        else if (selItem.equals("社团信息管理"))
            CommityInfoManage_.intent(this).Cid(this.Cid).UserType(thisUser.getUtype()).start();
        else if (selItem.equals("成员权限管理"))
            CommityMemManage_.intent(this).Cid(this.Cid).start();



    }






    //List View Animition
    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
