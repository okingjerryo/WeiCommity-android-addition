package com.learn.uitest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.learn.uitest.Model.testerModel;
import com.learn.uitest.R;
import com.learn.uitest.Utils.GetFromServer;
import com.learn.uitest.Utils.HttpJson;
import com.learn.uitest.Utils.MyAsyncTask;
import org.androidannotations.annotations.*;
import org.joda.time.LocalDate;
import org.json.JSONException;


import java.sql.Timestamp;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Extra
    String jsonString;


    @ViewById(R.id.backgroud)
    TextView backView ;

    @ViewById(R.id.register)
    CircularProgressButton button;
    //用AfterView 代替onCreate

    @AfterViews
    void update(){
        boolean flag = false;
        HttpJson json= new HttpJson();

        try {
            json = new HttpJson(jsonString,testerModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        testerModel model = (testerModel) json.getClassObject();
        LocalDate date = model.getDate();
        backView.setText("现在时刻"+date.toString()+"\n输入数据： "+model.getMessage());

    }

    //发送表单给服务器测试
    @Click
    void register(){
        button.setIndeterminateProgressMode(true);
        MyAsyncTask myAsyncTask = new MyAsyncTask(new MyAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(HttpJson output) {
                //先Resolve
                try {
                    output.resolveJsonString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (output.getStatusCode() == 400){

                }else {
                    button.setProgress(-1);
                }
            }
        }) {
            @Override
            protected Object doInBackground(Object... objects) {
                //1 定义
                HttpJson jsonSender = new HttpJson();
                button.setProgress(20);
                //2.塞变量
                jsonSender.setMessage("11111");
                try {
                    jsonSender.resolveJsonString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                button.setProgress(50);
                //3.发送~
                return new GetFromServer().execute("api/test",jsonSender.getJsonString());

            }

        };

        button.setProgress(-1);
    }

}
