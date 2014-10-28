package com.example.localdatastorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	public final static String LOGTAG="EXPLORECA";
	public final static String USERNAME="pref_username";
	public final static String VIEWIMAGES="pref_viewimages";
	
	private SharedPreferences settings;
	
	private OnSharedPreferenceChangeListener listener;
	
	private File file;
	private static final String FILENAME = "jsondata";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmlpull);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		listener = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				MainActivity.this.refreshDisplay(null);
			}			
		};
//		refreshDisplay(null); // Show preferences settings on preflisten example
		settings.registerOnSharedPreferenceChangeListener(listener);
		
		ToursPullParser parser = new ToursPullParser();
		List<Tour> tours = parser.parseXML(this);
		
		ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,android.R.layout.simple_list_item_1, tours);
		setListAdapter(adapter);
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
	
	public void createFile(View v) throws IOException, JSONException {
		
		if (!checkExternalStorage()) {
			return;
		}
		
		JSONArray data = getNewJSONData();
		
		String text = data.toString();
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(text.getBytes());
		fos.close();
		
		UIHelper.displayText(this, R.id.textView1, "File written to disk:\n" + data.toString());		
	}

	private JSONArray getNewJSONData() throws JSONException {
		JSONArray data = new JSONArray();
		JSONObject tour;
		
		tour = new JSONObject();
		tour.put("tour", "Salton Sea");
		tour.put("price", 900);
		data.put(tour);
		
		tour = new JSONObject();
		tour.put("tour", "Death Valley");
		tour.put("price", 600);
		data.put(tour);
		
		tour = new JSONObject();
		tour.put("tour", "San Francisco");
		tour.put("price", 1200);
		data.put(tour);
		return data;
	}
	
	public boolean checkExternalStorage() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			UIHelper.displayText(this, R.id.textView1, "External storage is read-only");
		}
		else {
			UIHelper.displayText(this, R.id.textView1, "External storage is unavailable");
		}
		return false;
	}
	
	public void readFile(View v) throws IOException, JSONException {
		/* Codes for internal settings file
		FileInputStream fis = openFileInput("myfile.txt");
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuffer b = new StringBuffer();
		while (bis.available() != 0) {
			char c = (char) bis.read();
			b.append(c);
		}
		
		UIHelper.displayText(this, R.id.textView1, b.toString());
		bis.close();
		fis.close();
		*/
		
		//json file
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuffer b = new StringBuffer();
		while (bis.available() != 0) {
			char c = (char) bis.read();
			b.append(c);
		}
		bis.close();
		fis.close();
		
		JSONArray data = new JSONArray(b.toString());
		
		StringBuffer toursBuffer = new StringBuffer();		
		for (int i = 0; i < data.length(); i++) {
			String tour = data.getJSONObject(i).getString("tour");
			toursBuffer.append(tour + "\n");
		}
		
		UIHelper.displayText(this, R.id.textView1, toursBuffer.toString());
	}
}
