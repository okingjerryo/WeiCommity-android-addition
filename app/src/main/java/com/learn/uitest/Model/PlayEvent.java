package com.learn.uitest.Model;

import com.learn.uitest.Service.MusicPlayer;

/**
 * PackageName com.learn.uitest.Model
 * Created by uryuo on 17/5/15.
 */
public class PlayEvent {

    public enum Action {
        PLAY, STOP, RESUME, NEXT, PREVIOES, SEEK,INIT,SEEKEND
    }

    private Action mAction;
    private String mSong;
    private int seekTo;
    private int thisPosition;
    private MusicPlayer thisPlayer;

    public Action getmAction() {
        return mAction;
    }

    public void setmAction(Action mAction) {
        this.mAction = mAction;
    }

    public String getmSong() {
        return mSong;
    }

    public void setmSong(String mSong) {
        this.mSong = mSong;
    }

    public int getThisPosition() {
        return thisPosition;
    }

    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

    public String getSong() {
        return mSong;
    }

    public void setSong(String song) {
        mSong = song;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }


    public MusicPlayer getThisPlayer() {
        return thisPlayer;
    }

    public void setThisPlayer(MusicPlayer thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public int getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(int seekTo) {
        this.seekTo = seekTo;
    }
}
