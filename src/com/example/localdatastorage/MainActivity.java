package com.example.localdatastorage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public final static String LOGTAG="EXPLORECA";
	public final static String USERNAME="pref_username";
	public final static String VIEWIMAGES="pref_viewimages";
	
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	public void setPreference (View v) {
		Log.i(LOGTAG, "Click set");
		
		/* Code for Java pref
		SharedPreferences.Editor editor = settings.edit();
		String prefValue = UIHelper.getText(this, R.id.editText1);
		editor.putString(USERNAME, prefValue);
		editor.commit();
		UIHelper.displayText(this, R.id.textView1, "Preference saved");
		*/
		
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void refreshDisplay (View v) {
		Log.i(LOGTAG, "Click show");
		
		String prefValue = settings.getString(USERNAME, "Not found");
		UIHelper.displayText(this, R.id.textView1, prefValue);
		UIHelper.setCBChecked(this, R.id.checkBox1, settings.getBoolean(VIEWIMAGES, false));
	}
}
