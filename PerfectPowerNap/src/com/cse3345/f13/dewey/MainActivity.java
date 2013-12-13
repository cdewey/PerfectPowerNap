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
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	AlarmManager am;
	PendingIntent pi;
    BroadcastReceiver br;
    PowerManager pm;
    WakeLock wl;
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
                nap("power");
            }
        });
		
		Button timedNapButton = (Button) findViewById(R.id.timedNapButton);
		timedNapButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nap("timed");
            }
        });
		
		Button endNap = (Button) findViewById(R.id.endNap);
		endNap.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                endNap();
            }
        });
		
		Button returnNap = (Button) findViewById(R.id.returnNap);
		returnNap.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	returnNap();
            }
        });
		
		NumberPicker np = (NumberPicker) findViewById(R.id.sleepTimePicker);
		np.setMaxValue(60);
		np.setMinValue(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(this,SettingsActivity.class);
				startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void nap(String type){
		
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		SharedPreferences.Editor edit = settings.edit();
	    edit.putBoolean("alramFinished", false);
	    edit.commit(); //apply
	    
		am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
		
		if(type == "power"){
			long alarmLength = calculateAlarmLength();
			am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmLength, pi );
		}
		
		else{
			NumberPicker np = (NumberPicker) findViewById(R.id.sleepTimePicker);
			long fallAsleepTime = np.getValue();
			am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + fallAsleepTime*60000, pi );
		}
		
		Context context = getApplicationContext();
		CharSequence napStart = "Your nap has started";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, napStart, duration);
		toast.show();
		
		Intent intent = new Intent(this,Nap.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
    }
	
	
	public long calculateAlarmLength(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		long fallAsleepTime = settings.getLong("fallAsleepTime", 0);
		long napLength = settings.getLong("napLength", 0);
		long offset = settings.getLong("offset", 0);
		long alarmLength = fallAsleepTime + napLength + offset;
		return alarmLength;
	}
	
	private void setup() {
	      br = new BroadcastReceiver() {
	             @Override
	             public void onReceive(Context c, Intent i) {
	            	wl.acquire();
	            	openAlarmScreen();
	            	wl.release();
                }
          };
	      registerReceiver(br, new IntentFilter("com.cse3345.f13.dewey"));
	      Intent intent = new Intent("com.cse3345.f13.dewey");
	      pi = PendingIntent.getBroadcast(this, 0, intent,0 );
	      am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
	      pm = (PowerManager) getSystemService(POWER_SERVICE);
	      wl = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "FlashActivity");
	}

	
	public void openAlarmScreen(){
		 Intent intent = new Intent(this,WakeUp.class);
		 startActivity(intent);
	}
	
	public void endNap(){
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0);
		SharedPreferences.Editor edit = settings.edit();
	    edit.putBoolean("alramFinished", false);
	    edit.putBoolean("napStarted", false);
	    edit.commit(); //apply
		
	    am.cancel(pi);
	    
		Context context = getApplicationContext();
		CharSequence napStart = "Nap Ended";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, napStart, duration);
		toast.show();
		
		
		Button powerNapButton = (Button) findViewById(R.id.powerNapButton);
    	Button timedNapButton = (Button) findViewById(R.id.timedNapButton);
    	Button endNap = (Button) findViewById(R.id.endNap);
    	Button returnNap = (Button) findViewById(R.id.returnNap);
    	powerNapButton.setEnabled(true);
    	timedNapButton.setEnabled(true);
    	endNap.setVisibility(endNap.INVISIBLE);
    	returnNap.setVisibility(returnNap.INVISIBLE);
	}
	
	public void returnNap(){
		Intent intent = new Intent(this,Nap.class);
		startActivity(intent);
	}
	@Override
	   public void onResume() {
			SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		    boolean nap = settings.getBoolean("napStarted", false);
		    if(nap == true){
		    	Button powerNapButton = (Button) findViewById(R.id.powerNapButton);
		    	Button timedNapButton = (Button) findViewById(R.id.timedNapButton);
		    	Button endNap = (Button) findViewById(R.id.endNap);
		    	Button returnNap = (Button) findViewById(R.id.returnNap);
		    	powerNapButton.setEnabled(false);
		    	timedNapButton.setEnabled(false);
		    	endNap.setVisibility(endNap.VISIBLE);
		    	returnNap.setVisibility(returnNap.VISIBLE);
		    }

	       super.onResume();
	   }
	@Override
	protected void onDestroy() {
	       am.cancel(pi);
	       unregisterReceiver(br);
	       wl.release();
	       super.onDestroy();
	}
}

