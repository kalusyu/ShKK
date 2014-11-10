package com.shower;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity{

	public static final String PREF_DATE_SAVE_KEY = "pref_date_save_key";
	public static final String PREF_DATE_SAVE = "pref_date_save";
	public static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";
	
	
	interface OnShowerDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *            with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onDateChanged(Calendar cal);
    }
	
	OnShowerDateChangedListener mDateChangeListener = new OnShowerDateChangedListener() {
		
		@Override
		public void onDateChanged(Calendar cal) {
			mYearText.setText(cal.get(Calendar.YEAR) + "");
			mMonthText.setText(cal.get(Calendar.MONTH) + 1 + "");
			mDayText.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
			mHourText.setText(cal.get(Calendar.HOUR_OF_DAY) + "");
			mMinuteText.setText(cal.get(Calendar.MINUTE) + "");
			
			// according to the save date
			mController.setSkin(SkinController.CHUN);
		}
	};
	
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
	
	View mFirst;
	View mScond;
	
	LinearLayout mDateTimePickerBg;
	ImageView addMonth;
	ImageView reduceMonth;
	ImageView addDay;
	ImageView reduceDay;
	ImageView addHour;
	ImageView reduceHour;
	ImageView addMinute;
	ImageView reduceMinute;
	Calendar mCurrentDate = Calendar.getInstance();
	
	TextView mYearText,mMonthText,mDayText,mHourText,mMinuteText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mShower = new ShowerImpl();
		mController = new SkinController();

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 1024 * 600
		setContentView(R.layout.main);
		initUI();
		initWindowSize();
		
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

		mDateTimePickerBg = (LinearLayout) dateView.findViewById(R.id.datetime_picker_ui_id);
		initPopupUI(dateView);
		setupSaveData();
		mDatetimePicker = new PopupWindow(dateView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mDatetimePicker.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.datetime_picker_bg)); 
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
				mDateTimePickerBg.setBackgroundResource(R.drawable.datetime_picker);
				setAddReduceVisible(Integer.MAX_VALUE);
				saveDate();
			}

			private void saveDate() {
				String date = new SimpleDateFormat(FORMATTER).format(mCurrentDate.getTime());
				SharedPreferences sp = getSharedPreferences(PREF_DATE_SAVE, Context.MODE_PRIVATE);
				sp.edit().putString(PREF_DATE_SAVE_KEY, date).apply();
			}
		});
	}
	
	// get date from share preferences
	private void setupSaveData() {
		SharedPreferences sp = getSharedPreferences(PREF_DATE_SAVE, Context.MODE_PRIVATE);
		String date = sp.getString(PREF_DATE_SAVE_KEY, new SimpleDateFormat(FORMATTER).format(Calendar.getInstance().getTime()));
		DateFormat df = new SimpleDateFormat(FORMATTER);
		try {
			Date d = df.parse(date);
			mCurrentDate.setTime(d);
			notifyDateChanged();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void addMonth(View v) {
		int oldVal = mCurrentDate.get(Calendar.MONTH);
		int newVal = oldVal + 1;
		onChangeMonth(oldVal, newVal);
	}
	
	public void reduceMonth(View v) {
		int oldVal = mCurrentDate.get(Calendar.MONTH);
		int newVal = oldVal - 1;
		onChangeMonth(oldVal, newVal);
	}
	
	public void onChangeMonth(int oldVal, int newVal){
		if (oldVal == 11 && newVal == 0) {
            mCurrentDate.add(Calendar.MONTH, 1);
        } else if (oldVal == 0 && newVal == 11) {
            mCurrentDate.add(Calendar.MONTH, -1);
        } else {
            mCurrentDate.add(Calendar.MONTH, newVal - oldVal);
        }
		notifyDateChanged();
	}
	
	
	
	public void notifyDateChanged(){
		mDateChangeListener.onDateChanged(mCurrentDate);
	}

	public void addDay(View v) {
		int oldVal = mCurrentDate.get(Calendar.DAY_OF_MONTH);
		int newVal = oldVal + 1;
		onChangeDay(oldVal, newVal);
	}

	public void reduceDay(View v) {
		int oldVal = mCurrentDate.get(Calendar.DAY_OF_MONTH);
		int newVal = oldVal - 1;
		onChangeDay(oldVal, newVal);
	}
	
	public void onChangeDay(int oldVal,int newVal){
		int maxDayOfMonth = mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (oldVal == maxDayOfMonth && newVal == 1) {
            mCurrentDate.add(Calendar.DAY_OF_MONTH, 1);
        } else if (oldVal == 1 && newVal == maxDayOfMonth) {
            mCurrentDate.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            mCurrentDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
        }
        notifyDateChanged();
	}

	public void addHour(View v) {
		int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
		hour += 1;
		int newHour = Math.min(hour, 23);
		onChangeHour(newHour);
	}

	public void reduceHour(View v) {
		int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
		hour -= 1;
		int newHour = Math.max(hour, 0);
		onChangeHour(newHour);
	}
	
	public void onChangeHour(int newVal){
		mCurrentDate.set(Calendar.HOUR_OF_DAY, newVal);
		notifyDateChanged();
	}

	public void addMinute(View v) {
		int minute = mCurrentDate.get(Calendar.MINUTE);
		minute += 1;
		int newMinute = Math.min(minute, 59);
		onChangeMinute(newMinute);
	}

	public void reduceMinute(View v) {
		int minute = mCurrentDate.get(Calendar.MINUTE);
		minute -= 1;
		int newMinute = Math.max(minute, 0);
		onChangeMinute(newMinute);
	}
	
	public void onChangeMinute(int newVal){
		mCurrentDate.set(Calendar.MINUTE, newVal);
		notifyDateChanged();
	}
	
	private void initPopupUI(View dateView) {
		addMonth = (ImageView)dateView.findViewById(R.id.addMonth);
		reduceMonth = (ImageView)dateView.findViewById(R.id.reduceMonth);
		addDay = (ImageView)dateView.findViewById(R.id.addDay);
		reduceDay = (ImageView)dateView.findViewById(R.id.reduceDay);
		addHour = (ImageView)dateView.findViewById(R.id.addHour);
		reduceHour = (ImageView)dateView.findViewById(R.id.reduceHour);
		addMinute = (ImageView)dateView.findViewById(R.id.addMinute);
		reduceMinute = (ImageView)dateView.findViewById(R.id.reduceMinute);
		
		mYearText = (TextView) dateView.findViewById(R.id.year);
		mMonthText = (TextView) dateView.findViewById(R.id.month);
		mDayText = (TextView) dateView.findViewById(R.id.day);
		mHourText = (TextView) dateView.findViewById(R.id.hour);
		mMinuteText = (TextView) dateView.findViewById(R.id.minute);
	}

	public static final int SHOWER_MONTH = -101;
	public static final int SHOWER_DAY = -102;
	public static final int SHOWER_HOUR = -103;
	public static final int SHOWER_MINUTE = -104;
	public void monthOnClick(View v) {
		mDateTimePickerBg.setBackgroundResource(R.drawable.datetime_picker_month);
		setAddReduceVisible(SHOWER_MONTH);
	}

	public void dayOnClick(View v) {
		mDateTimePickerBg.setBackgroundResource(R.drawable.datetime_picker_day);
		setAddReduceVisible(SHOWER_DAY);
	}

	public void hourOnClick(View v) {
		mDateTimePickerBg.setBackgroundResource(R.drawable.datetime_picker_hour);
		setAddReduceVisible(SHOWER_HOUR);
	}

	public void minuteOnClick(View v) {
		mDateTimePickerBg.setBackgroundResource(R.drawable.datetime_picker_minute);
		setAddReduceVisible(SHOWER_MINUTE);
	}
	
	private void setAddReduceVisible(int showerMonth) {
		switch(showerMonth){
		case SHOWER_MONTH:
			addMonth.setVisibility(View.VISIBLE);
			reduceMonth.setVisibility(View.VISIBLE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			break;
		case SHOWER_DAY:
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.VISIBLE);
			reduceDay.setVisibility(View.VISIBLE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			break;
		case SHOWER_HOUR:
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.VISIBLE);
			reduceHour.setVisibility(View.VISIBLE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			break;
		case SHOWER_MINUTE:
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.VISIBLE);
			reduceMinute.setVisibility(View.VISIBLE);
			break;
			default:
				addMonth.setVisibility(View.GONE);
				reduceMonth.setVisibility(View.GONE);
				addDay.setVisibility(View.GONE);
				reduceDay.setVisibility(View.GONE);
				addHour.setVisibility(View.GONE);
				reduceHour.setVisibility(View.GONE);
				addMinute.setVisibility(View.GONE);
				reduceMinute.setVisibility(View.GONE);
				break;
		}
	}
	
	
	
	
	
	public void modelPhoto(View v){
		mFirst = findViewById(R.id.first_main_ui_id);
		mScond = findViewById(R.id.second_main_ui_id);
		dismissAnima(mFirst);
	}
	
	public void modelText(View v){
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setView(getLayoutInflater().inflate(R.layout.model_edit_ui_layout,null));
//		AlertDialog dialog = builder.create();
//		Window win = dialog.getWindow();
//		WindowManager.LayoutParams lp = win.getAttributes();
//		lp.x = 200;//设置x坐标
//		lp.y = 100;//设置y坐标
//		lp.width = 288;
//		lp.height = 233;
//		win.setAttributes(lp);
//		builder.show();
//		((TextView)v).setText("");
	}
	
	
	public void modelLeft(View v){
		mPager.arrowScroll(17);
	}
	
	public void modelRight(View v){
		mPager.arrowScroll(66);
	}

	
	// useless
	public void displayAnima(View v1) {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.zoomin);
		v1.startAnimation(anim);
	}

	public void dismissAnima(View v0) {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mFirst.setVisibility(View.GONE);
				mScond.setVisibility(View.VISIBLE);
				displayAnima(mScond);
			}
		});
		v0.startAnimation(anim);
	}
	

}
