package com.learn.uitest.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.alibaba.fastjson.TypeReference;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.learn.uitest.Model.CommityInfo;
import com.learn.uitest.Model.CommityMember;
import com.learn.uitest.R;
import com.learn.uitest.Service.ShowDielog;
import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.StaticVar;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EActivity(R.layout.activity_commity_mem_manage)
public class CommityMemManage extends AppCompatActivity implements ObservableScrollViewCallbacks {
    @Extra
    String Cid = StaticVar.thisCid;

    int Utype;
    //社团全部成员
    List<CommityMember> allMember = new ArrayList<CommityMember>();

    @ViewById(R.id.CMMpeople_list)
    ObservableListView listView;
    @AfterViews
    void init(){
        listView.setScrollViewCallbacks(this);
        //获得当前社团全部人员
        getAllCommityMem();
    }

    @Background
    void getAllCommityMem(){
        //1.封装
        String thisAPi = "api/commity/userInCommity/getALL";
        //2.装Json
        HttpJson json = new HttpJson();
        CommityMember sendI = new CommityMember();
        sendI.setCid(this.Cid);
        //3.封装
        json.setClassName("CommityMember:CommityMem-get");
        json.setClassObject(sendI);
        json.constractJsonString();
        //4.发送
        HttpJson re = GetFromServer.execute(StaticVar.connectionTar+thisAPi,json.getJsonString());
        getAllResult(re);
    }

    @UiThread
    void getAllResult(HttpJson re){
        try {
            if (re.getStatusCode() != 400)
                throw new IllegalAccessException("");

                re.resolveJsonObjectStrng(new TypeReference<List<CommityMember>>(){});
                allMember = new ArrayList<CommityMember>((Collection<? extends CommityMember>) re.getClassObject());
                constractShowList();
        }catch (IllegalAccessException e){
            ShowDielog.showAlertNormal(this,"服务器出粗",re.getMessage());
        }catch (Exception e){
            ShowDielog.showAlertNormal(this,"客户端出错",e.getMessage());
        }

    }
    private void constractShowList(){
        ArrayList<String> items = new ArrayList<String>();
        for (CommityMember thisMem:allMember){
            if (thisMem.getUUuid().equals(StaticVar.thisUUuid))
                Utype = thisMem.getUtype();
            items.add(thisMem.getUNackName()+"        职位"+thisMem.getUTypeName());
        }
        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, items));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              CommityMember targetP =  allMember.get(position);
              CommityMemManage_Detail_.intent(CommityMemManage.this).CMid(targetP.getCMid()).thisUtype(Utype).start();

            }
        });
    }


    //滚动条事件
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

