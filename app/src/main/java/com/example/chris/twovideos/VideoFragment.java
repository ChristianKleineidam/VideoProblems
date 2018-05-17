package com.example.chris.twovideos;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
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
import com.google.android.exoplayer2.upstream.RawResourceDataSource;


public class VideoFragment extends Fragment implements SurfaceHolder.Callback{
    private Context mContext;
    private SimpleExoPlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videolayout, container, false);
        SurfaceView mSurfaceView = (SurfaceView) view.findViewById(R.id.surface_view1);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setSizeFromLayout();
        surfaceHolder.addCallback(this);

        mContext = inflater.getContext();

        return view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mPlayer.setVideoSurfaceHolder(holder);
        mPlayer.setPlayWhenReady(true);
        playVideo(mContext);
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
