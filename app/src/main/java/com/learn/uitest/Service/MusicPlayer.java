package com.learn.uitest.Service;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * PackageName com.learn.uitest.Service
 * Created by uryuo on 17/5/15.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    private static MusicPlayer player = new MusicPlayer();
    private MediaPlayer mMediaPlayer;
    private Context mContext;
//    private List<Song> mQueue;
    private int mQueueIndex;
    private PlayMode mPlayMode;
    private String thisfile;


    private enum PlayMode {
        LOOP, RANDOM, REPEAT
    }

    public static MusicPlayer getPlayer() {
        return player;

    }

    public static void setPlayer(MusicPlayer player) {
        MusicPlayer.player = player;
    }

    public MusicPlayer() {

        mMediaPlayer = new ManagedMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);

//        mQueue = new ArrayList<>();
//        mQueueIndex = 0;

        mPlayMode = PlayMode.LOOP;
    }

    public void startPlay(String thisSong) {
        play(thisSong);
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }
    public void play(String path ) {
        try {
            thisfile = path;
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void resume() {
        mMediaPlayer.start();
    }

    public void stop(){mMediaPlayer.stop();}
    public void next() {
        play(getNextSong());
    }

    public void previous() {
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    private String getNowPlaying() {
        return thisfile;
    }

    private String getNextSong() {
      return thisfile;
    }

    private String getPreviousSong() {
      return  getNextSong();
    }

    public void seekto(int p){
        mMediaPlayer.seekTo(p);
        mMediaPlayer.start();
    }
    public int getCurrentPosition() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

//    private int getNextIndex() {
//        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
//        return mQueueIndex;
//    }
//
//    private int getPreviousIndex() {
//        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
//        return mQueueIndex;
//    }

//    private int getRandomIndex() {
//        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
//        return mQueueIndex;
//    }

    private void release() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        mContext = null;
    }

}
