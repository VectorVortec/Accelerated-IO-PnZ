package com.acceleratedio.pac_n_zoom;

import android.content.Context;
import android.media.MediaPlayer.OnCompletionListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Intent;
import android.media.MediaPlayer.OnPreparedListener;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import de.greenrobot.event.EventBus;
import android.media.MediaPlayer.OnVideoSizeChangedListener;

public class ViewVideos extends Activity implements SurfaceHolder.Callback {

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean pausing = false;
    ProgressDialog progress;
    VideoView mVideoView;
    Button bback;
    private int position = 0;
    Button mPlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewvideo);
        int position = getIntent().getIntExtra("position", -1);
        String vidFileName = PickAnmActivity.fil_nams[position].replace('/', '?') + ".mp4";
        String httpAddrs = "https://www.pnzanimate.me/Droid/db_rd.php?";
        httpAddrs += vidFileName;
        mVideoView = (VideoView) findViewById(R.id.vviidd);
				//mVideoView.setZOrderMediaOverlay(true);
				//mVideoView.setZOrderOnTop(true);
				mVideoView.setVisibility(View.VISIBLE);
				progress = new ProgressDialog(ViewVideos.this);
				progress = ProgressDialog.show(this, "Loading the video", "dialog message", true);
				progress.show();
				progress.setCancelable(true);

        try {
					// Start the MediaController
					MediaController mediacontroller = new MediaController(ViewVideos.this);
					mediacontroller.setMediaPlayer(mVideoView);
					mediacontroller.setAnchorView(mVideoView);
					mVideoView.setMediaController(mediacontroller);
					surfaceHolder = mVideoView.getHolder();
					Uri video = Uri.parse(httpAddrs);
					mVideoView.setVideoURI(video);
					getWindow().setFormat(PixelFormat.TRANSLUCENT);

					mVideoView.setOnPreparedListener(new OnPreparedListener() {

						public void onPrepared(MediaPlayer mp) {

							if (mp.getVideoHeight() == 0) {

								mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

                  @Override
                  public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                    
										// TODO Auto-generated method stub
										progress.dismiss();
										mVideoView.requestFocus();
										mVideoView.start();
                  }
                });
              } else { 
								progress.dismiss();
								mVideoView.requestFocus();
								mVideoView.start();
							}
						}
					});           

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        bback = (Button) this.findViewById(R.id.bback);
        bback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", mVideoView.getCurrentPosition());
        mVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        mVideoView.seekTo(position);
    }

    public class AlphaListUpdateEvent{
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
         
    }		
}
