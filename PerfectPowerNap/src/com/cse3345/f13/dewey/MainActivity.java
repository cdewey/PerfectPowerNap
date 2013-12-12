package com.cse3345.f13.dewey;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	AlarmManager am;
	PendingIntent pi;
    BroadcastReceiver br;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences runCheck = getSharedPreferences("powerNapSettings", 0); //load the preferences
		Boolean hasRun = runCheck.getBoolean("hasRun", false); //see if it's run before, default no
		if (!hasRun) {
		    SharedPreferences settings = getSharedPreferences("powerNapSettings", 0);
		    SharedPreferences.Editor edit = settings.edit();
		    edit.putBoolean("hasRun", true); //set to has run
		    edit.commit(); //apply
		    Intent intent = new Intent(this,SetUp.class);
			startActivity(intent);
		}
		setContentView(R.layout.activity_main);
		setup();
		Button powerNapButton = (Button) findViewById(R.id.powerNapButton);
		powerNapButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                powerNap(v);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void powerNap(View view){	
		int alarmLengthMin = calculateAlarmLength();
		long alarmLengthMs = alarmLengthMin*60000; //convert mins to ms
		am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
		am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, pi );
		
    }
	
	public int calculateAlarmLength(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		int fallAsleepTime = settings.getInt("fallAsleepTime", 0);
		int napLength = settings.getInt("napLength", 0);
		int offset = settings.getInt("offset", 0);
		int alarmLength = fallAsleepTime + napLength + offset;
		return alarmLength;
	}
	
	private void setup() {
	      br = new BroadcastReceiver() {
	             @Override
	             public void onReceive(Context c, Intent i) {
	                    Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();
	                    }
	             };
	      registerReceiver(br, new IntentFilter("com.cse3345.f13.dewey") );
	      pi = PendingIntent.getBroadcast( this, 0, new Intent("com.cse3345.f13.dewey"),0 );
	      am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
	}


	@Override
	protected void onDestroy() {
	       am.cancel(pi);
	       unregisterReceiver(br);
	       super.onDestroy();
	}
}

