package com.learn.uitest.Activity;


import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.learn.uitest.Model.testerModel;
import com.learn.uitest.R;
import com.learn.uitest.Utils.HttpJson;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import org.joda.time.LocalDate;


/**
 * PackageName com.learn.uitest
 * Created by uryuo on 17/5/4.
 */
@EActivity(R.layout.main)
public class FirstAnnoActivity extends Activity {
    @ViewById(R.id.myInput)
    EditText myInput;

    @ViewById(R.id.myTextView)
    TextView textView;

    //LocalDate date = LocalDate.now();
    HttpJson json = new HttpJson();
    @Click
    void login(){
        String name = myInput.getText().toString();

        json.setMessage(name);
        textView.setText("Hello "+json.getMessage());
    }

    @Click
    void jump(){
        String message = myInput.getText().toString();
        //MainActivity_.intent(this).testMessage(message).start();
        testerModel tranmodel = new testerModel();
        tranmodel.setDate(LocalDate.now());
        tranmodel.setMessage(myInput.getText().toString());
        tranmodel.setNum(12);
        HttpJson trans = new HttpJson();
        trans.setClassObject(tranmodel);
        trans.constractJsonString();
        MainActivity_.intent(this).jsonString(trans.getJsonString()).start();
    }

}
