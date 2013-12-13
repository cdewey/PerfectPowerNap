package com.cse3345.f13.dewey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class WakeUp extends Activity {
	 MediaPlayer mediaPlayer = new MediaPlayer();
	 String typeNap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wake_up);
		
		Intent intent = getIntent();
		typeNap = intent.getStringExtra("type");
		 
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
		if(typeNap == "power"){
			RelativeLayout info = (RelativeLayout) findViewById(R.id.background);
			info.setVisibility(info.VISIBLE);
		}
		else{
			finish();
		}
	}
	
	public void startAlarm(){
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer=MediaPlayer.create(this,R.raw.siren_noise);
		mediaPlayer.setLooping(true);
        mediaPlayer.start();
	}

	public void updateOffset(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		Long offset = settings.getLong("offset", 0);
		
		RadioGroup rg = (RadioGroup) findViewById(R.id.yesNO);
		if(rg.getCheckedRadioButtonId()!=-1){
		    int id= rg.getCheckedRadioButtonId();
		    RadioButton radioButton = (RadioButton) findViewById(id);
		    int index = rg.indexOfChild(radioButton);		    
		    if (index == 0){
		    	offset = offset + 30000;
		    }
		}
		
		SharedPreferences.Editor edit = settings.edit();
	    edit.putLong("offset", offset);
	    edit.commit(); //apply
	    finish();
	}
	
	public void onBackPressed() {
	    super.onBackPressed();
	    finish();
	}
}
