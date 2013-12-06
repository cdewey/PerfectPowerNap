package com.cse3345.f13.dewey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences runCheck = getSharedPreferences("hasRunBefore", 0); //load the preferences
		Boolean hasRun = runCheck.getBoolean("hasRun", false); //see if it's run before, default no
		if (!hasRun) {
		    SharedPreferences settings = getSharedPreferences("hasRunBefore", 0);
		    SharedPreferences.Editor edit = settings.edit();
		    edit.putBoolean("hasRun", true); //set to has run
		    edit.commit(); //apply
		    //code for if this is the first time the app has run
		    Intent intent = new Intent(this,SetUp.class);
			startActivityForResult(intent,0);
		}
		/*if (Appconfig.activity == false) {
			 Intent intent = new Intent(this,SetUp.class);
			 startActivityForResult(intent,0);
		}*/
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}

