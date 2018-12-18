package com.hutchind.cordova.plugins.streamingmedia;

import android.content.Context;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.graphics.Color;
import java.nio.ByteBuffer;
import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;

public class RtspPlayer extends Activity implements MediaPlayer.MediaPlayerCallback {
    private static ViewGroup viewGroup;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // viewGroup = (ViewGroup)
        // ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
        // play("rtsp://admin:11ghalad.@192.168.2.134:554/third");
        RelativeLayout relLayout = new RelativeLayout(this);
        relLayout.setBackgroundColor(Color.BLACK);
        RelativeLayout.LayoutParams relLayoutParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mediaPlayer = new veg.mediaplayer.sdk.MediaPlayer(this);
        mediaPlayer.setLayoutParams(relLayoutParam);
        relLayout.addView(mediaPlayer);
		setContentView(relLayout, relLayoutParam);

        Bundle b = getIntent().getExtras();
        if (b != null){
                play(b.getString("url"));
        }

    }

    public void play(String url) {

        if(url.equals("") || url == null)
            return;

        Context context = getApplicationContext();
        handler = new Handler();
        try {
            mediaPlayer.getSurfaceView().setZOrderOnTop(true);
            SurfaceHolder trackHolder = mediaPlayer.getSurfaceView().getHolder();
            trackHolder.setFormat(android.R.color.transparent);
            MediaPlayerConfig config = new MediaPlayerConfig();
            config.setConnectionUrl(url);
            config.setConnectionNetworkProtocol(-1);
            config.setConnectionBufferingTime(300);
            config.setConnectionDetectionTime(1000);
            config.setDecodingType(1);
            config.setRendererType(1);
            config.setSynchroEnable(1);
            config.setSynchroNeedDropVideoFrames(1);
            config.setEnableColorVideo(1);
            config.setEnableAspectRatio(1);
            config.setDataReceiveTimeout(30000);
            config.setNumberOfCPUCores(0);
            mediaPlayer.Open(config, this);
            final long streamPosition = mediaPlayer.getStreamPosition();
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mediaPlayer.getStreamPosition() == streamPosition) {
                            Toast.makeText(getApplicationContext(), "NEED SOME ERROR TEXT" + streamPosition,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        // finish();
                    }
                }
            };
            handler.postDelayed(runnable, 6000);
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mediaPlayer != null)
            mediaPlayer.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null)
            mediaPlayer.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null)
            mediaPlayer.Close();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mediaPlayer != null)
            mediaPlayer.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mediaPlayer != null)
            mediaPlayer.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.onDestroy();
    }

    @Override
    public int Status(int i) {
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }
}
