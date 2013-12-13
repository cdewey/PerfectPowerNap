package com.cse3345.f13.dewey;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class WakeUp extends Activity {
	 MediaPlayer mediaPlayer = new MediaPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wake_up);
		
		startAlarm();
		
		Button stopButton = (Button) findViewById(R.id.stop);
		stopButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	v.setVisibility(v.INVISIBLE);
                endAlarm();
            }
        });
		
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                updateOffset();
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wake_up, menu);
		return true;
	}
	
	public void endAlarm(){
		mediaPlayer.stop();
		RelativeLayout info = (RelativeLayout) findViewById(R.id.background);
		info.setVisibility(info.VISIBLE);
	}
	
	public void startAlarm(){
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer=MediaPlayer.create(this,R.raw.siren_noise);
        mediaPlayer.start();
	}

	public void updateOffset(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		int offset = settings.getInt("offset", 0);
		SharedPreferences.Editor edit = settings.edit();
	    edit.putInt("offset", offset);
	    edit.commit(); //apply
	    finish();
	}
}