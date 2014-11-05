package com.shower;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
		setCurrentFlow(1);
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

	public void templatrueReduce(View v){
		if (setTemplatureTag(-1))
			handleTemplature(-1);
	}
	

	public void templatruePlus(View v){
		if(setTemplatureTag(1))
			handleTemplature(mFlowRate);
	}
	
	public void flowReduce(View v){
		if(setFlowTag(-1))
			handleFlow(mFlowRate);
	}
	
	public void flowPlus(View v){
		if(setFlowTag(1))
			handleFlow(1);
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
