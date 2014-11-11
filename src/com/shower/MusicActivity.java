package com.shower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MusicActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_layout);
	}
	
	public void backToShower(View v){
		this.finish();
	}
	
	public void downloadMusi(View v){
		
	}
}
