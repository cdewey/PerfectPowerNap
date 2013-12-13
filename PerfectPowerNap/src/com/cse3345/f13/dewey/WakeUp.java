package com.cse3345.f13.dewey;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class WakeUp extends Activity {
	 MediaPlayer mediaPlayer = new MediaPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wake_up);
	    
		startAlarm();
		
		ImageButton stopButton = (ImageButton) findViewById(R.id.stop);
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
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		SharedPreferences.Editor edit = settings.edit();
	    edit.putBoolean("alramFinished", true);
	    edit.putBoolean("napStarted", false);
	    edit.commit(); //apply
	    
		mediaPlayer.stop();
		RelativeLayout info = (RelativeLayout) findViewById(R.id.background);
		info.setVisibility(info.VISIBLE);
	}
	
	public void startAlarm(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0);
		String path = settings.getString("alertTone", "N/A");
		Uri alarmUri = Uri.parse(path);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer=MediaPlayer.create(this,alarmUri);
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
		    	SharedPreferences.Editor edit = settings.edit();
			    edit.putLong("offset", offset);
			    edit.commit(); //apply
		    }
		}
		
		
	    finish();
	}
	
	public void onBackPressed() {
		endAlarm();
	    super.onBackPressed();
	    finish();
	}
	
	@Override
	   public void onDestroy() {
		    
	       if (mediaPlayer != null){
	    	   mediaPlayer.release();
	       }
	       super.onDestroy();
	   }
}
