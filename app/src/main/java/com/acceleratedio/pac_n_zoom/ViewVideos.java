package com.acceleratedio.pac_n_zoom;

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
import android.widget.VideoView;
import android.content.Intent;

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


//Implement SurfaceHolder interface to Play video
//Implement this interface to receive information about changes to the surface
public class ViewVideos extends Activity implements SurfaceHolder.Callback{

	MediaPlayer mediaPlayer;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean pausing = false;;
	public static ProgressDialog progress;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewvideo);

		getWindow().setFormat(PixelFormat.UNKNOWN);

		//Displays a video file.   
		VideoView mVideoView = (VideoView)findViewById(R.id.vviidd);
		String uriPath = "com.acceleratedio.pac_n_zoom.ViewVideos";
		Uri uri = Uri.parse(uriPath);
		mVideoView.setVideoURI(uri);
		mVideoView.requestFocus();
		mVideoView.start();

		progress = ProgressDialog.show(this, "Loading the animation", "dialog message", true);
		MakePostRequest get_video = new MakePostRequest();
    get_video.execute();
  }

  public class MakePostRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
			String vidFileName;
			InputStream in = null;
			String result = "fail";
			int position = getIntent().getIntExtra("position", -1);
			vidFileName = PickAnmActivity.fil_nams[position].replace('/', '?') + ".mp4";
			String httpAddrs = "https://meme.svgvortec.com/Droid/db_rd.php?";
			httpAddrs += vidFileName; 
			File sdCard = Environment.getExternalStorageDirectory(); // Find sdcard
			File dir = new File (sdCard.getAbsolutePath() + "/acceleratedio/pacnzoom");
			dir.mkdirs();

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpRequest = new HttpGet(httpAddrs);
				HttpResponse response = httpClient.execute(httpRequest);
				in = response.getEntity().getContent();

				File file = new File(dir, "/" + vidFileName); // Instantiate the file class
				FileOutputStream f = new FileOutputStream(file); // Open the file

				byte[] buffer = new byte[1024];
				int len1 = 0;

				while ((len1 = in.read(buffer)) > 0) {                          
						f.write(buffer, 0, len1);               
				}

				f.close();
				progress.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				progress.dismiss();
			} finally {
				progress.dismiss();
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return result;
		}

		protected void onPostExecute(String vidFileName) {
			super.onPostExecute(vidFileName);
     	dsply_video(vidFileName);
		}
	}
	
	private void dsply_video(String video) {
		Log.d("dsply_video", "The video is loaded.");

	}

	public void bbaacckk(View view){
		Intent intent = new Intent(this, PickAnmActivity.class);
		startActivity(intent);
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
