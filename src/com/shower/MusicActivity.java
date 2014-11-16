package com.shower;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MusicActivity extends Activity{
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_layout);
		TextView musicText = (TextView) findViewById(R.id.music_text);
		TextView modelText = (TextView) findViewById(R.id.model_text);
		int type = getIntent().getIntExtra("type", SkinController.CHUN);
		View v = findViewById(R.id.music_layout_background);
		if (type == SkinController.CHUN){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_chun_normal);
			musicText.setTextColor(Color.parseColor("#53680e"));
			modelText.setTextColor(Color.parseColor("#53680e"));
		} else if (type == SkinController.XIA){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_xia_normal);
			musicText.setTextColor(Color.parseColor("#ffffff"));
			modelText.setTextColor(Color.parseColor("#ffffff"));
		} else if (type == SkinController.QIU){
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_qiu_normal);
			musicText.setTextColor(Color.parseColor("#3f300c"));
			modelText.setTextColor(Color.parseColor("#3f300c"));
		}  else {
			v.setBackgroundResource(R.drawable.yinyuexiazaixuanzejiemian_dong_normal);
			musicText.setTextColor(Color.parseColor("#405b69"));
			modelText.setTextColor(Color.parseColor("#405b69"));
		}
	}
	
	public void backToShower(View v){
		this.finish();
	}
	
	public void downloadMusi(View v){
		
	}
}
