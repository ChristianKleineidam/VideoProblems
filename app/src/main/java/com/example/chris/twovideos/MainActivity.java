package com.example.chris.twovideos;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    public SimpleExoPlayer mPlayer;
    private DataSource.Factory mDataSourceFactory;
    private boolean videoChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

                mPlayer = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector);
                if (videoChoice){
                    playVideo(MainActivity.this, "collect");
                }
                else{
                    playVideo(MainActivity.this, "chair");
                }
                videoChoice = !videoChoice;
                waitTillReady();
            }
        });
    }

    private void waitTillReady(){
        if (mPlayer.getPlaybackState() == Player.STATE_READY){
            createNewFragment();
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    waitTillReady();
                }
            }, 10);
        }
    }




    private void createNewFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contentFragment, new VideoFragment());
        ft.commit();
    }

    private DataSource.Factory getDataSourceFactory(){
        if (mDataSourceFactory==null){
            mDataSourceFactory = new DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, this.getPackageName()) ) ;
        }

        return mDataSourceFactory;
    }

    private void playVideo(Context context, String name){
        final int raw_res_id = context.getResources().getIdentifier(
                name,
                "raw",
                context.getPackageName());
        MediaSource media_source = new ExtractorMediaSource.Factory(getDataSourceFactory()).createMediaSource(
                RawResourceDataSource.buildRawResourceUri(raw_res_id));
        mPlayer.prepare(media_source);
        mPlayer.setPlayWhenReady(false);

    }
}
