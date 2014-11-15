package com.shower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MusicActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_layout);
		int type = getIntent().getIntExtra("type", SkinController.CHUN);
		View v = findViewById(R.id.music_layout_background);
		if (type == SkinController.CHUN){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_chun_normal);
		} else if (type == SkinController.XIA){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_xia_normal);
		} else if (type == SkinController.QIU){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_qiu_normal);
		}  else {
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_dong_normal);
		}
	}
	
	public void backToShower(View v){
		this.finish();
	}
	
	public void downloadMusi(View v){
		
	}
}
