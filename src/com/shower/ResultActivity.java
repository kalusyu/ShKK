package com.shower;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		Typeface tf  = Typeface.createFromAsset(getAssets(), "fonts/fangzhengyaoti.ttf");
		TextView time = (TextView)findViewById(R.id.txt_time);
		TextView flow = (TextView)findViewById(R.id.txt_flow);
		TextView temp = (TextView)findViewById(R.id.txt_temperature);
		int timeD = getIntent().getIntExtra("timeD", 12);
		int flowD = getIntent().getIntExtra("flowD", 96);
		int tempD = getIntent().getIntExtra("tempD", 22);
		time.setTypeface(tf);
		flow.setTypeface(tf);
		temp.setTypeface(tf);
		time.setText(String.format("淋浴时间%ds", timeD));
		flow.setText(String.format("淋浴水量%dL", flowD));
		temp.setText(String.format("淋浴温度%d℃", tempD));
		
		((TextView)findViewById(R.id.today_shower_data)).setTypeface(tf);
		TextView cmLogo = (TextView)findViewById(R.id.cm_logo);
		cmLogo.setTypeface(tf);
		CharSequence cs = cmLogo.getText();
		SpannableString ss = new SpannableString(cs);
		ss.setSpan(new RelativeSizeSpan(1.2f), 8, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.BLUE), 8, 14,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		cmLogo.setText(ss);
	}
}
