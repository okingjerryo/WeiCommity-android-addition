package com.learn.uitest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;
import butterknife.ButterKnife;
import com.learn.uitest.Model.PlayEvent;
import com.learn.uitest.R;
import com.learn.uitest.Service.MusicPlayer;
import com.learn.uitest.Service.PlayService;
import com.learn.uitest.Utils.StaticVar;
import com.yalantis.audio.lib.AudioUtil;
import com.yalantis.waves.util.Horizon;
import org.androidannotations.annotations.*;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_music_play)
public class MusicPlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @ViewById(R.id.play)
    Button play;

    @ViewById(R.id.timeList)
    TextView timeLine;

    @ViewById(R.id.seekbar)
    SeekBar bar;
    MusicPlayer player = new MusicPlayer();
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
    private int counter=0;
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = 1;
    private static final int RECORDER_ENCODING_BIT = 16;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_DECIBELS = 120;

    private AudioRecord audioRecord;
    private Horizon mHorizon;
    private GLSurfaceView glSurfaceView;
    private int startCP;
    private int endCP;
    private Thread recordingThread;
    private byte[] buffer;

    boolean touchFlag = false;
    String thisSongLenth = "";
    PlayService musicPlay = new PlayService();
    PlayEvent thisEvent = new PlayEvent();
    Timer timer = new Timer();
    boolean flag = false;

    private AudioRecord.OnRecordPositionUpdateListener recordPositionUpdateListener = new AudioRecord.OnRecordPositionUpdateListener() {
        @Override
        public void onMarkerReached(AudioRecord recorder) {
            //empty for now
        }

        @Override
        public void onPeriodicNotification(AudioRecord recorder) {
            if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING
                    && audioRecord.read(buffer, 0, buffer.length) != -1) {
                mHorizon.updateView(buffer);
            }
        }
    };



    @AfterViews
    void init(){
        ButterKnife.bind(this);
        startService(new Intent(this, PlayService.class));
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface);
        mHorizon = new Horizon(glSurfaceView, getResources().getColor(R.color.background),
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);
        mHorizon.setMaxVolumeDb(MAX_DECIBELS);
        bar.setMax(100);
        bar.setOnSeekBarChangeListener(this);
    }

    @Click({R.id.play})
    void click(View view){
        PlayEvent playEvent;
        switch (view.getId()) {
            case R.id.play:
                thisEvent = new PlayEvent();

                thisEvent.setThisPlayer(player);
                thisEvent.setAction(PlayEvent.Action.INIT);
                EventBus.getDefault().post(thisEvent);
                thisEvent.setSong(StaticVar.connectionTar+"fileSpace/ProjectSpace/c016dc80-4eac-4c3d-9632-c41693ad4f8c/test.wav");
                thisEvent.setAction(PlayEvent.Action.PLAY);
                bar.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(thisEvent);
                if (!flag) {
                    begin();
                    flag = true;
                }
                break;
        }
    }

    //时间更新
    public void begin() {
        timer.schedule(updateTime, 0, 117);
    }


    @Touch(R.id.next)
    void nextT(MotionEvent event){
        if (player.isPlaying()){

            endCP = player.getCurrentPosition();
                play.setText(endCP + "");

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(!touchFlag){
                        touchFlag = true;
                        startCP = player.getCurrentPosition();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                   touchFlag = false;
                   if (endCP-startCP>100)
                   play.setText(startCP+"-"+endCP);
                   else
                       play.setText(startCP+"");
                   break;
            }
        }
    }


    private TimerTask updateTime = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player.isPlaying()) {
                        if (thisSongLenth.isEmpty()) {
                            thisSongLenth = getMilstoTimeFormat(player.getDuration());
                        }
                        String positon = getMilstoTimeFormat(player.getCurrentPosition());
                        timeLine.setText(positon + "/" + thisSongLenth);
                        //更新seek
                        double thisP = player.getCurrentPosition();
                        double allP = player.getDuration();
                        bar.setProgress((int) ((thisP / allP) * 100));
                    }
                }
            });
        }
    };

    public void timerCancal(){
        timer.cancel();
    }
    @Click
    void seek(){
        bar.setVisibility(View.INVISIBLE);
        timeLine.setText("0:00.000/0:00:000");
        play.setText("开始/暂停");
        thisEvent.setAction(PlayEvent.Action.STOP);
        EventBus.getDefault().post(thisEvent);


    }



    @Click(R.id.next)
    void next(){
        int a =player.getCurrentPosition();
        play.setText(a+"");

    }
    @Override
    protected void onStart() {
        super.onStart();
        checkPermissionsAndStart();
        EventBus.getDefault().register(musicPlay);
    }
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        if(player.getDuration()!=0) {
            thisEvent.setAction(PlayEvent.Action.RESUME);
            EventBus.getDefault().post(thisEvent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        thisEvent.setAction(PlayEvent.Action.SEEK);
        EventBus.getDefault().post(thisEvent);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(musicPlay);
        super.onStop();
        if (audioRecord != null) {
            audioRecord.release();
        }
        thisEvent.setAction(PlayEvent.Action.STOP);
        EventBus.getDefault().post(thisEvent);
        AudioUtil.disposeProcessor();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndStart();
                } else {
                    finish();
                }
        }

    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION_RECORD_AUDIO);
        } else {
            initRecorder();
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                startRecording();
            }
        }
    }

    private void initRecorder() {
        final int bufferSize = 2 * AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);
        AudioUtil.initProcessor(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);

        recordingThread = new Thread("recorder") {
            @Override
            public void run() {
                super.run();
                buffer = new byte[bufferSize];
                Looper.prepare();
                audioRecord.setRecordPositionUpdateListener(recordPositionUpdateListener, new Handler(Looper.myLooper()));
                int bytePerSample = RECORDER_ENCODING_BIT / 8;
                float samplesToDraw = bufferSize / bytePerSample;
                audioRecord.setPositionNotificationPeriod((int) samplesToDraw);
                //We need to read first chunk to motivate recordPositionUpdateListener.
                //Mostly, for lower versions - https://code.google.com/p/android/issues/detail?id=53996
                audioRecord.read(buffer, 0, bufferSize);
                Looper.loop();
            }
        };
    }

    private void startRecording() {
        if (audioRecord != null) {
            audioRecord.startRecording();
        }
        recordingThread.start();
    }

    private String getMilstoTimeFormat(int time){
        int min;
        int sec;
        int mils;
        min = time/60000;
        time%=60000;
        sec = time/1000;
        time %=1000;
        mils = time;

        String secStr = ZeroAdder(sec,false);
        String milsStr = ZeroAdder(mils,true);
        String minStr = ZeroAdder(min,false);
        return minStr+":"+secStr+"."+milsStr;
    }

    private String ZeroAdder(int a,boolean isSec){
        if (a<10){
            if (isSec)
                return "00"+a;
            return "0"+a;
        }else if (a<100){
            if (isSec)
                return "0"+a;
        }
        return  a+"";
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!player.isPlaying()) {
            double partion = (double) progress / (double) 100;
            thisEvent.setThisPosition((int) (player.getDuration() * partion));
            String thisText = getMilstoTimeFormat(thisEvent.getThisPosition());
            timeLine.setText(thisText + "/" + thisSongLenth);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        thisEvent.setAction(PlayEvent.Action.SEEK);
        EventBus.getDefault().post(thisEvent);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        thisEvent.setAction(PlayEvent.Action.SEEKEND);
        EventBus.getDefault().post(thisEvent);
    }
}
