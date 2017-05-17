package com.learn.uitest.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.learn.uitest.Model.PlayEvent;
import org.androidannotations.annotations.EService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * PackageName com.learn.uitest.Service
 * Created by uryuo on 17/5/15.
 */
@EService
public class PlayService extends Service {

    private int flag = -1;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //接收EventBus post过来的PlayEvent
    @Subscribe
    public int onEvent(PlayEvent playEvent) {
        switch (playEvent.getAction()) {
            case PLAY:
                if(flag==-1){
                    MusicPlayer.getPlayer().startPlay(playEvent.getSong());
                    flag =1;
                }else if (flag ==1) {
                    MusicPlayer.getPlayer().pause();
                    flag =0;
                }else {
                    MusicPlayer.getPlayer().resume();
                    flag=1;
                }
                break;
            case SEEK:
                MusicPlayer.getPlayer().pause();
                flag = 0;
               break;
            case RESUME:
                MusicPlayer.getPlayer().resume();
                flag = 1;
                break;
            case INIT:
                MusicPlayer.setPlayer(playEvent.getThisPlayer());
                break;
            case STOP:
                MusicPlayer.getPlayer().stop();
                flag = -1;
                break;
            case SEEKEND:
                MusicPlayer.getPlayer().seekto(playEvent.getThisPosition());
                flag = 1;
            break;
        }
        return 0;
    }
}
