package com.shower;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity{

	RelativeLayout mTemplatureUI;
	TextView mTemplatureTen,mTemplatureUnit;
	int mTemplature;
	int mOldTemplature;
	
	ImageView mFlowScrollBar;
	
	SkinController mController;
	
	int mFlowRate,mOldFlowRate;
	
	public static final int ON = 101;
	public static final int OFF = 100;
	
	public static final int DINGPENG = 2;
	public static final int CEPENG = 2 << 1;
	public static final int PUBU = 2 << 2;
	public static final int SHOUCHI = 2 << 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 1024 * 600
		setContentView(R.layout.main);
		initUI();
		mController = new SkinController();
		if (1==1){
			mController.setSkin(SkinController.CHUN);
		}
		setCurrentTemplatrue(98);//38 test
		setTemplatureTag(0);
		handleTemplature(0);
		setCurrentFlow(1); // 1 test
		setFlowTag(0);
		handleFlow(1);
	}
	
	private void handleFlow(int i) {
		handleUI(i);
	}

	private void handleUI(int i) {
		int identifyFlow= getResources().getIdentifier(String.format("liuliangtiao%d_chun_normal", i), "drawable", "com.shower");
		mFlowScrollBar.setImageResource(identifyFlow);
	}

	private boolean setFlowTag(int plusReduce) {
		mOldFlowRate = mFlowRate = (Integer)mFlowScrollBar.getTag();
		mFlowRate += plusReduce;
		if (mFlowRate != 4 && mFlowRate != 0){
			mFlowScrollBar.setTag(mFlowRate);
			return true;
		} else {
			return false;
		}
	}

	// i should be 1 || 2 || 3
	private void setCurrentFlow(int i) {
		mFlowScrollBar.setTag(i);
	}

	private void setCurrentTemplatrue(int templature) {
		mTemplatureUI.setTag(templature);
	}

	private void initUI() {
		mTemplatureUI = (RelativeLayout) findViewById(R.id.wendu_ui_show);
		mTemplatureTen = (TextView) findViewById(R.id.templature_shi);
		mTemplatureUnit = (TextView) findViewById(R.id.templature_ge);
		mFlowScrollBar = (ImageView) findViewById(R.id.flow_scroll_bar);
	}

	// templature
	public void templatrueReduce(View v){
		if (setTemplatureTag(-1))
			handleTemplature(-1);
	}
	

	public void templatruePlus(View v){
		if(setTemplatureTag(1))
			handleTemplature(1);
	}
	
	// flow 
	public void flowReduce(View v){
		if(setFlowTag(-1))
			handleFlow(mFlowRate);
	}
	
	public void flowPlus(View v){
		if(setFlowTag(1))
			handleFlow(mFlowRate);
	}
	
	//shower button
	public void cepeng(View v) {
		ImageView img = (ImageView)v;
		handShower(img, CEPENG);
	}

	public void dingpeng(View v) {
		ImageView img = (ImageView)v;
		handShower(img, DINGPENG);
	}

	public void pubu(View v) {
		ImageView img = (ImageView)v;
		handShower(img, PUBU);
	}

	public void shouchi(View v) {
		ImageView img = (ImageView)v;
		handShower(img, SHOUCHI);
	}
	
	public void handShower(ImageView img,int type){
		Object tag = img.getTag();
		if (tag == null){
			img.setTag(ON);
			switch(type){
			case CEPENG:
				img.setImageResource(R.drawable.cepeng_btn_on);
				break;
			case DINGPENG:
				img.setImageResource(R.drawable.dingpeng_btn_on);
				break;
			case PUBU:
				img.setImageResource(R.drawable.pubu_btn_on);
				break;
			case SHOUCHI:
				img.setImageResource(R.drawable.shouchi_btn_on);
				break;
			}
		} else {
			if (tag instanceof Integer){
				int status = (Integer)tag;
				if (status == ON){
					img.setTag(OFF);
					if (type == CEPENG) img.setImageResource(R.drawable.cepeng_btn);
					if (type == DINGPENG) img.setImageResource(R.drawable.dingpeng_btn);
					if (type == PUBU) img.setImageResource(R.drawable.pubu_btn);
					if (type == SHOUCHI) img.setImageResource(R.drawable.shouchi_btn);
				} else {
					img.setTag(ON);
					if (type == CEPENG) img.setImageResource(R.drawable.cepeng_btn_on);
					if (type == DINGPENG) img.setImageResource(R.drawable.dingpeng_btn_on);
					if (type == PUBU) img.setImageResource(R.drawable.pubu_btn_on);
					if (type == SHOUCHI) img.setImageResource(R.drawable.shouchi_btn_on);
				}
			}
		}
	}
	
	private void handleTemplature(int i) {
		int ten = mOldTemplature/10;
		int unit = mOldTemplature%10;
		switch(i){
		case -1:
			if (unit == 0){
				if (ten > 0){
					ten -= 1;
					unit = 9;
				}
			} else {
				unit -= 1;
			}
			break;
		case 1:
			if (unit == 9){
				if (ten < 9){
					ten += 1;
					unit = 0;
				}
			} else {
				unit += 1;
			}
			break;
			default:
				break;
		}
		
		handleUI(ten,unit);
	}
	
	//wendushuzi0_chun_normal.png
	private void handleUI(int ten, int unit) {
		int identifyTen= getResources().getIdentifier(String.format("wendushuzi%d_chun_normal", ten), "drawable", "com.shower");
		int identifyUnit= getResources().getIdentifier(String.format("wendushuzi%d_chun_normal", unit), "drawable", "com.shower");
		mTemplatureTen.setBackgroundResource(identifyTen);
		mTemplatureUnit.setBackgroundResource(identifyUnit);
	}

	public boolean setTemplatureTag(int plusReduce){
		mOldTemplature = mTemplature = (Integer)mTemplatureUI.getTag();
		mTemplature += plusReduce;
		if (mTemplature != 100 && mTemplature!=-1){
			mTemplatureUI.setTag(mTemplature);
			return true;
		} else {
			return false;
		}
	}
}
