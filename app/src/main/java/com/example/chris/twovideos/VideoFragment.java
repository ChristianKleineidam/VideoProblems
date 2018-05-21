package com.example.chris.twovideos;


import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;


public class VideoFragment extends Fragment{
    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private SurfaceView mSurfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videolayout, container, false);
        mContext = inflater.getContext();

        mSurfaceView = view.findViewById(R.id.surface_view1);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mPlayer.setVideoSurfaceView(mSurfaceView);
        mPlayer.setPlayWhenReady(true);
        playVideo(mContext);

        return view;
    }

    private void playVideo(Context context){
        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);

        int raw_res_id = context.getResources().getIdentifier(
                "collect",
                "raw",
                context.getPackageName());
        DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(raw_res_id));
        try {
            rawResourceDataSource.open(dataSpec);

            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };
            MediaSource media_source = new ExtractorMediaSource.Factory(factory).createMediaSource(rawResourceDataSource.getUri());
            mPlayer.prepare(media_source);

        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        mPlayer.release();
        super.onDestroyView();
    }

    private void playVideo2(Context context){
        DataSource.Factory factory = new DefaultDataSourceFactory(
                mContext,
                Util.getUserAgent(mContext, context.getPackageName()) ) ;
        Uri uri= Uri.parse("android.resource://"+context.getPackageName()+"/raw/collect");
        MediaSource media_source = new ExtractorMediaSource.Factory(factory).createMediaSource(uri);
        mPlayer.prepare(media_source);
    }
}
