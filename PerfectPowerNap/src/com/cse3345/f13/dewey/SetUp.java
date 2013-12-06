package com.cse3345.f13.dewey;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.NumberPicker;

public class SetUp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up);
		
		NumberPicker np = (NumberPicker) findViewById(R.id.sleepTimePicker);
		np.setMaxValue(100);
		np.setMinValue(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_up, menu);
		return true;
	}

}
