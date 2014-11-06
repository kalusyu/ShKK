package com.shower;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	RelativeLayout mTemplatureUI;
	TextView mTemplatureTen, mTemplatureUnit;
	int mTemplature;
	int mOldTemplature;

	ImageView mFlowScrollBar;

	SkinController mController;

	int mFlowRate, mOldFlowRate;

	IShower mShower;
	View dateView;
	PopupWindow mDatetimePicker;

	public static final int ON = 101;
	public static final int OFF = 100;

	public static final int DINGPENG = 2;
	public static final int CEPENG = 2 << 1;
	public static final int PUBU = 2 << 2;
	public static final int SHOUCHI = 2 << 3;
	public static final int LAUNCH = 2 << 4;

	int mWindowWidth;
	int mWindowHeight;
	
	static final int MODELSIZE = 5;
	int mCurrentModelPosition = 2;
	
	ViewPager mPager;
	ModelPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mShower = new ShowerImpl();

		super.onCreate(savedInstanceState);
		// 1024 * 600
		setContentView(R.layout.main);
		initUI();
		initWindowSize();
		mController = new SkinController();
		if (1 == 1) {
			mController.setSkin(SkinController.CHUN);
		}
		setCurrentTemplatrue(98);// 38 test
		// setCurrentTemplatrue(mShower.getTempalture()); // actual
		setTemplatureTag(0);
		handleTemplature(0);
		setCurrentFlow(1); // 1 test
		// setCurrentFlow(mShower.getFlow());// actual
		setFlowTag(0);
		handleFlow(1);
	}

	private void initWindowSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWindowWidth = metrics.widthPixels;
		mWindowHeight = metrics.heightPixels;
	}

	private void handleFlow(int i) {
		handleUI(i);
	}

	private void handleUI(int i) {
		int identifyFlow = getResources().getIdentifier(
				String.format("liuliangtiao%d_chun_normal", i), "drawable",
				"com.shower");
		mFlowScrollBar.setImageResource(identifyFlow);
	}

	private boolean setFlowTag(int plusReduce) {
		mOldFlowRate = mFlowRate = (Integer) mFlowScrollBar.getTag();
		mFlowRate += plusReduce;
		if (mFlowRate != 4 && mFlowRate != 0) {
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
		initPicker();
		initViewPager();
	}

	private void initViewPager() {
		
		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ModelPagerAdapter(this,MODELSIZE);
		mAdapter.setData(initData());
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(2);
	}

	private SparseArray<Model> initData() {
		int i = 0;
		SparseArray<Model> s = new SparseArray<Model>();
		Model model = new Model();
		model.modelName = "自定义五";
		model.modelResource = R.drawable.women_normal;
		s.put(i++, model);
		
		model = new Model();
		model.modelName = "常规一";
		model.modelResource = R.drawable.men_normal;
		s.put(i++, model);
		
		model = new Model();
		model.modelName = "常规二";
		model.modelResource = R.drawable.women_normal;
		s.put(i++, model);
		
		model = new Model();
		model.modelName = "常规三";
		model.modelResource = R.drawable.men_normal;
		s.put(i++, model);
		
		model = new Model();
		model.modelName = "自定义一";
		model.modelResource = R.drawable.women_normal;
		s.put(i++, model);
		return s;
	}

	// templature
	public void templatrueReduce(View v) {
		if (setTemplatureTag(-1)) {
			handleTemplature(-1);
			mShower.reduceTemplatuer(1);
		}
	}

	public void templatruePlus(View v) {
		if (setTemplatureTag(1)) {
			handleTemplature(1);
			mShower.addTemplatuer(1);
		}
	}

	// flow
	public void flowReduce(View v) {
		if (setFlowTag(-1)) {
			handleFlow(mFlowRate);
			mShower.reduceFlow(1);
		}
	}

	public void flowPlus(View v) {
		if (setFlowTag(1)) {
			handleFlow(mFlowRate);
			mShower.addFlow(1);
		}
	}

	// shower button
	public void cepeng(View v) {
		ImageView img = (ImageView) v;
		handShower(img, CEPENG);
	}

	public void dingpeng(View v) {
		ImageView img = (ImageView) v;
		handShower(img, DINGPENG);
	}

	public void pubu(View v) {
		ImageView img = (ImageView) v;
		handShower(img, PUBU);
	}

	public void shouchi(View v) {
		ImageView img = (ImageView) v;
		handShower(img, SHOUCHI);
	}

	public void zhongjian(View v) {
		ImageView img = (ImageView) v;
		handShower(img, LAUNCH);
	}

	public void handShower(ImageView img, int type) {
		Object tag = img.getTag();
		if (tag == null) {
			img.setTag(ON);
			switch (type) {
			case CEPENG:
				img.setImageResource(R.drawable.cepeng_btn_on);
				mShower.cepengOn();
				break;
			case DINGPENG:
				img.setImageResource(R.drawable.dingpeng_btn_on);
				mShower.dingpengOn();
				break;
			case PUBU:
				img.setImageResource(R.drawable.pubu_btn_on);
				mShower.pubuOn();
				break;
			case SHOUCHI:
				img.setImageResource(R.drawable.shouchi_btn_on);
				mShower.shouchiOn();
				break;
			case LAUNCH:
				img.setImageResource(R.drawable.zhongjian_btn_on);
				mShower.startShower();
				break;
			}
		} else {
			if (tag instanceof Integer) {
				int status = (Integer) tag;
				if (status == ON) {
					img.setTag(OFF);
					if (type == CEPENG) {
						mShower.cepengOff();
						img.setImageResource(R.drawable.cepeng_btn);
					}
					if (type == DINGPENG) {
						mShower.dingpengOff();
						img.setImageResource(R.drawable.dingpeng_btn);
					}
					if (type == PUBU) {
						mShower.pubuOff();
						img.setImageResource(R.drawable.pubu_btn);
					}
					if (type == SHOUCHI) {
						mShower.shouchiOff();
						img.setImageResource(R.drawable.shouchi_btn);
					}
					if (type == LAUNCH) {
						mShower.stopShower();
						img.setImageResource(R.drawable.zhongjian_btn);
					}
				} else {
					img.setTag(ON);
					if (type == CEPENG) {
						mShower.cepengOn();
						img.setImageResource(R.drawable.cepeng_btn_on);
					}
					if (type == DINGPENG) {
						mShower.dingpengOn();
						img.setImageResource(R.drawable.dingpeng_btn_on);
					}
					if (type == PUBU) {
						mShower.pubuOn();
						img.setImageResource(R.drawable.pubu_btn_on);
					}
					if (type == SHOUCHI) {
						mShower.shouchiOn();
						img.setImageResource(R.drawable.shouchi_btn_on);
					}
					if (type == LAUNCH) {
						mShower.startShower();
						img.setImageResource(R.drawable.zhongjian_btn_on);
					}
				}
			}
		}
	}

	private void handleTemplature(int i) {
		int ten = mOldTemplature / 10;
		int unit = mOldTemplature % 10;
		switch (i) {
		case -1:
			if (unit == 0) {
				if (ten > 0) {
					ten -= 1;
					unit = 9;
				}
			} else {
				unit -= 1;
			}
			break;
		case 1:
			if (unit == 9) {
				if (ten < 9) {
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

		handleUI(ten, unit);
	}

	// wendushuzi0_chun_normal.png
	private void handleUI(int ten, int unit) {
		int identifyTen = getResources().getIdentifier(
				String.format("wendushuzi%d_chun_normal", ten), "drawable",
				"com.shower");
		int identifyUnit = getResources().getIdentifier(
				String.format("wendushuzi%d_chun_normal", unit), "drawable",
				"com.shower");
		mTemplatureTen.setBackgroundResource(identifyTen);
		mTemplatureUnit.setBackgroundResource(identifyUnit);
	}

	public boolean setTemplatureTag(int plusReduce) {
		mOldTemplature = mTemplature = (Integer) mTemplatureUI.getTag();
		mTemplature += plusReduce;
		if (mTemplature != 100 && mTemplature != -1) {
			mTemplatureUI.setTag(mTemplature);
			return true;
		} else {
			return false;
		}
	}

	public void setDateTime(View v) {
		mDatetimePicker.showAtLocation(v, Gravity.RIGHT | Gravity.BOTTOM, 0, 100);
	}
	
	private void initPicker(){
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View dateView = inflater.inflate(R.layout.datetime_picker_ui, null);

		mDatetimePicker = new PopupWindow(dateView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mDatetimePicker.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.datetime_picker_background)); // 有这个 点击空白处才会消失
		mDatetimePicker.getBackground().setAlpha(0);
		mDatetimePicker.setOutsideTouchable(true);

		// use system animation
		mDatetimePicker.setAnimationStyle(android.R.style.Animation_Dialog);
		mDatetimePicker.update();
		mDatetimePicker.setTouchable(true);
		mDatetimePicker.setFocusable(true);

		mDatetimePicker.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// dismissDimBackground();
			}
		});
	}
	
	public void modelPhoto(View v){
		
	}
	
	public void modelText(View v){
		
	}
	
	
	public void modelLeft(View v){
		mPager.arrowScroll(17);
	}
	
	public void modelRight(View v){
		mPager.arrowScroll(66);
	}

	
	// useless
	public void displayAnima(View v1) {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		anim.setDuration(300);
		v1.setAnimation(anim);
		anim.start();
	}

	public void dismissAnima(View v0) {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		anim.setDuration(300);
		v0.setAnimation(anim);
		anim.start();
	}
	
	

}
