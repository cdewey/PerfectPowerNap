package com.cse3345.f13.dewey;

import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class Nap extends Activity {
	MediaPlayer mediaPlayer = new MediaPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nap);
		
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.soundChoice);        
	    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int id) {
	            setBackground(group,id);
	        }
	    });
	}

	public void setBackground(RadioGroup group, int id){
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.background);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		if(id == R.id.ocean){
			rl.setBackgroundResource(R.drawable.ocean);
			mediaPlayer=MediaPlayer.create(this,R.raw.ocean);
            mediaPlayer.start();
		}
		else if(id == R.id.fire){
			rl.setBackgroundResource(R.drawable.fire);
			mediaPlayer=MediaPlayer.create(this,R.raw.fire);
            mediaPlayer.start();
		}
		else if(id == R.id.thunder){
			rl.setBackgroundResource(R.drawable.lightning);
			mediaPlayer=MediaPlayer.create(this,R.raw.thunder);
            mediaPlayer.start();
		}
		else if(id == R.id.birds){
			rl.setBackgroundResource(R.drawable.bird);
			mediaPlayer=MediaPlayer.create(this,R.raw.birds);
            mediaPlayer.start();
		}
		else if(id == R.id.none){
			rl.setBackgroundColor(Color.BLACK);
			mediaPlayer.stop();
		}
	}
	
	
	public void onBackPressed() {
	    super.onBackPressed();
	    finish();
	}
	
	@Override
	   public void onPause() {
	       if (mediaPlayer != null){
	    	   mediaPlayer.pause();
	       }
	       super.onPause();
	   }
	@Override
	   public void onDestroy() {
	       if (mediaPlayer != null){
	    	   mediaPlayer.release();
	       }
	       super.onDestroy();
	   }

}
