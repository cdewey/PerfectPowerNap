package com.cse3345.f13.dewey;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetUp extends Activity {
	public String ringTonePath; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up);
		
		
		
		NumberPicker np = (NumberPicker) findViewById(R.id.sleepTimePicker);
		np.setMaxValue(60);
		np.setMinValue(0);
		
		Uri defaultAlert = Uri.parse("android.resource://com.cse3345.f13.dewey/" + R.raw.siren_noise);
		ringTonePath = defaultAlert.toString();
		
		Button selectSound = (Button) findViewById(R.id.alertSoundButton);
		selectSound.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                selectAlertSound(v);
            }
        });
		
		Button save = (Button) findViewById(R.id.doneButton);
		save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                saveSettings(v);
            }
        });

		
		
	}

	
	public void selectAlertSound(View view){
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select sound for alarm");
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
		startActivityForResult(intent,999);
    }
	
	@Override
	protected void onActivityResult(int resultNum,int resultCode, Intent data)
    {
		if (resultCode == RESULT_OK) {
		    Uri alertTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		    if (alertTone != null) {
			    ringTonePath = alertTone.toString();
			    Ringtone ringtone = RingtoneManager.getRingtone(this, alertTone);
			    String name = ringtone.getTitle(this);
			    Button selectSound = (Button) findViewById(R.id.alertSoundButton);
			    selectSound.setText(name);
		    }
		}
    	
    }
	
	public void saveSettings(View view){
		NumberPicker np = (NumberPicker) findViewById(R.id.sleepTimePicker);
		long fallAsleepTime = np.getValue();
		
		long napLength = 0;
		RadioGroup rg = (RadioGroup) findViewById(R.id.napLength);
		if(rg.getCheckedRadioButtonId()!=-1){
		    int id= rg.getCheckedRadioButtonId();
		    RadioButton radioButton = (RadioButton) findViewById(id);
		    int index = rg.indexOfChild(radioButton);
		    
		    if (index == 0){
		    	napLength = 10;
		    }
		    else if (index == 1){
		    	napLength = 15;
		    }
		    if (index == 2){
		    	napLength = 20;
		    }
		}
		
		long offset = 0;
		
		SharedPreferences settings = getSharedPreferences("powerNapSettings", 0); //load the preferences
		SharedPreferences.Editor edit = settings.edit();
	    edit.putLong("fallAsleepTime", fallAsleepTime*60000);
	    edit.putLong("napLength", napLength*60000);
	    edit.putLong("offset", offset*60000);
	    edit.putString("alertTone", ringTonePath);
	    edit.commit(); //apply
	    finish();
		
		
		
    }

}
