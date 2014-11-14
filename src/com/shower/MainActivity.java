package com.shower;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.shower.SkinController.SkinCallbacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SkinCallbacks{

	public static final String PREF_SAVE = "com.shower.prefer.save";
	public static final String PREF_DATE_SAVE_KEY = "pref_date_save_key";
	public static final String PREF_MODEL_SAVE_KEY = "pref_model_save_key";
	public static final String PREF_TEMP_SAVE_KEY = "pref_temp_save_key";
	public static final String PREF_FLOW_SAVE_KEY = "pref_flow_save_key";
	public static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_MODEL_DATA = "自定义（五）:女人;常规模式:男人;常规模式:女人;常规模式:男人;自定义（一）:女人;";
	
	interface OnShowerDateChangedListener {

        /**
         * Called upon a date change.
         *
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
			changeSkin(cal);
		}
	};
	
	
	RelativeLayout mTemplatureUI;
	TextView mTemplatureTen, mTemplatureUnit;
	int mTemplature;
	int mOldTemplature;

	ImageView mFlowScrollBar;

	SkinController mController;
	
	View mBackground;
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
	
	// model select
	ViewPager mPager;
	ModelPagerAdapter mAdapter;
	
	// model edit
	PopupWindow mModelEditPop;
	String mModelName;
	String mSexy;
	static final String MAN = "男";
	static final String WOMEN = "女";
	static final String NORMAL_MODEL = "常规模式";
	static final String CUSTOM_MODEL_ONE = "自定义（一）";
	static final String CUSTOM_MODEL_FIVE = "常规模式（五）";
	SparseArray<Model> mMap;
	int mBeforeSettingPosition = -1;
	
	View mFirst;
	View mScond;
	
	// date time
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
		mController = new SkinController(this);

		super.onCreate(savedInstanceState);
		
		// 1024 * 600
		setContentView(R.layout.main);
		initUI();
		initWindowSize();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mFirst = findViewById(R.id.first_main_ui_id);
		mScond = findViewById(R.id.second_main_ui_id);
		mFirst.setVisibility(View.VISIBLE);
		mScond.setVisibility(View.GONE);
		setUIEnable(false);
	}
	

	private void setUIEnable(boolean b) {
		View date = findViewById(R.id.main_datetime_text_show);
		View color = findViewById(R.id.main_color_show);
		View music = findViewById(R.id.main_music_show);
		View lanya = findViewById(R.id.main_lanya_show);
		date.setEnabled(b);
		color.setEnabled(b);
		music.setEnabled(b);
		lanya.setEnabled(b);
	}

	private void initTempFlowData() {
		int current = mPager.getCurrentItem();
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		int temp = sp.getInt(PREF_TEMP_SAVE_KEY+current, mShower.getTempalture());//mShower.getTempalture() default templature
		setCurrentTemplatrue(temp);
		setTemplatureTag(0);
		handleTemplature(0);
		int flow = sp.getInt(PREF_FLOW_SAVE_KEY+current, mShower.getFlow());
		setCurrentFlow(flow); 
		setFlowTag(0);
		handleFlow(flow);
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
		mBackground = findViewById(R.id.main_bg);
		mTemplatureUI = (RelativeLayout) findViewById(R.id.wendu_ui_show);
		mTemplatureTen = (TextView) findViewById(R.id.templature_shi);
		mTemplatureUnit = (TextView) findViewById(R.id.templature_ge);
		mFlowScrollBar = (ImageView) findViewById(R.id.flow_scroll_bar);
		initPicker();
		initViewPager();
		initEditModel();
	}

	private void initEditModel() {
		View dateView = getLayoutInflater().inflate(R.layout.model_edit_ui_layout, null);
		initEditModelUI(dateView);
		mModelEditPop = new PopupWindow(dateView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//		mModelEditPop.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.datetime_picker_bg)); 
//		mModelEditPop.getBackground().setAlpha(0);
//		mModelEditPop.setOutsideTouchable(false); // set false

		// use system animation
		mModelEditPop.setAnimationStyle(android.R.style.Animation_Dialog);
		mModelEditPop.update();
		mModelEditPop.setTouchable(true);
		mModelEditPop.setFocusable(true);

	}

	/**
	 * 
	 * KaluYu
	 * @param dateView
	 * 2014年11月10日 下午9:39:57
	 */
	private void initEditModelUI(View dateView) {
		Model model = mMap.get(mPager.getCurrentItem());
		EditText modelEdit = (EditText) dateView.findViewById(R.id.model_edit_name);
		modelEdit.setText(model.modelName);
		modelEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				mModelName = s.toString() != null ? s.toString() 
						: (mPager.getCurrentItem() > 2 ? CUSTOM_MODEL_ONE : CUSTOM_MODEL_FIVE);
			}
		});
		
		RadioGroup rg = (RadioGroup) dateView.findViewById(R.id.model_edit_rg);
		RadioButton rbm = (RadioButton) dateView.findViewById(R.id.model_edit_rb_men);
		RadioButton rbw = (RadioButton) dateView.findViewById(R.id.model_edit_rb_women);
		if(model.modelResource == R.drawable.men_normal){
			 rbm.setChecked(true);
		} else {
			 rbw.setChecked(true);
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.model_edit_rb_men){
					mSexy = MAN;
				} else if (checkedId == R.id.model_edit_rb_women){
					mSexy = WOMEN;
				}
			}
		});
		
		ImageView ok = (ImageView) dateView.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveEidtedModelData();
				Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
				mModelEditPop.dismiss();
				mBeforeSettingPosition = mPager.getCurrentItem();
				initViewPager();
			}
		});
		
		ImageView cancel = (ImageView) dateView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mModelEditPop.dismiss();
			}
		});
	}
	
	public void saveEidtedModelData(){
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		int position = mPager.getCurrentItem();
		Model m = mMap.get(position);
		m.modelName = mModelName;
		m.mSexy = mSexy; 
		StringBuilder sb = new  StringBuilder();
		for (int i=0; i < mMap.size(); i++){
			sb.append(mMap.get(i).toString());
			if (i != mMap.size() - 1){
				sb.append(";");
			}
		}
		sp.edit().putString(PREF_MODEL_SAVE_KEY, sb.toString()).apply();
	}

	private void initViewPager() {
		
		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ModelPagerAdapter(this,MODELSIZE);
		mMap = initData();
		mAdapter.setData(mMap);
		mPager.setAdapter(mAdapter);
		if (mBeforeSettingPosition != -1){
			mPager.setCurrentItem(mBeforeSettingPosition);
		} else {
			mPager.setCurrentItem(2);
		}
	}

	private SparseArray<Model> initData() {
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		String data = sp.getString(PREF_MODEL_SAVE_KEY, DEFAULT_MODEL_DATA);
		String[] dataArray = data.split(";");
		SparseArray<Model> s = new SparseArray<Model>();
		for (int i = 0, size = dataArray.length; i < size; i++){
			String[] custom = dataArray[i].split(":");
			Model model = new Model();
			model.modelName = custom[0];
			model.modelResource = custom[1].equals(MAN) ? R.drawable.men_normal:R.drawable.women_normal;
			s.put(i, model);
		}
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
		handShower(img, CEPENG, R.drawable.cepeng_btn, R.drawable.cepeng_btn_on);
	}

	public void dingpeng(View v) {
		ImageView img = (ImageView) v;
		handShower(img, DINGPENG, R.drawable.dingpeng_btn, R.drawable.dingpeng_btn_on);
	}

	public void pubu(View v) {
		ImageView img = (ImageView) v;
		handShower(img, PUBU, R.drawable.pubu_btn, R.drawable.pubu_btn_on);
	}

	public void shouchi(View v) {
		ImageView img = (ImageView) v;
		handShower(img, SHOUCHI,R.drawable.shouchi_btn,R.drawable.shouchi_btn_on);
	}

	public void zhongjian(View v) {
		ImageView img = (ImageView) v;
		handShower(img, LAUNCH,R.drawable.zhongjian_btn,R.drawable.zhongjian_btn_on);
	}

	@Override
	public void handShower(ImageView img, int type, int resourceNormal,int resourceOn) {
		Object tag = img.getTag();
		if (tag == null) {
			img.setTag(ON);
			img.setImageResource(resourceOn);
			switch (type) {
			case CEPENG:
				mShower.cepengOn();
				break;
			case DINGPENG:
				mShower.dingpengOn();
				break;
			case PUBU:
				mShower.pubuOn();
				break;
			case SHOUCHI:
				mShower.shouchiOn();
				break;
			case LAUNCH:
				mShower.startShower();
				break;
			}
		} else {
			if (tag instanceof Integer) {
				int status = (Integer) tag;
				if (status == ON) {
					img.setTag(OFF);
					img.setImageResource(resourceNormal);
					if (type == CEPENG) {
						mShower.cepengOff();
					}
					if (type == DINGPENG) {
						mShower.dingpengOff();
					}
					if (type == PUBU) {
						mShower.pubuOff();
					}
					if (type == SHOUCHI) {
						mShower.shouchiOff();
					}
					if (type == LAUNCH) {
						mShower.stopShower();
					}
				} else {
					img.setTag(ON);
					img.setImageResource(resourceOn);
					if (type == CEPENG) {
						mShower.cepengOn();
					}
					if (type == DINGPENG) {
						mShower.dingpengOn();
					}
					if (type == PUBU) {
						mShower.pubuOn();
					}
					if (type == SHOUCHI) {
						mShower.shouchiOn();
					}
					if (type == LAUNCH) {
						mShower.startShower();
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
				String.format(mController.tempDrawableName, ten), "drawable",
				"com.shower");
		int identifyUnit = getResources().getIdentifier(
				String.format(mController.tempDrawableName, unit), "drawable",
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
				SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
				sp.edit().putString(PREF_DATE_SAVE_KEY, date).apply();
			}
		});
	}
	
	// get date from share preferences
	private void setupSaveData() {
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
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
		TextView timeShow = (TextView)findViewById(R.id.main_datetime_text_show);
		DateFormat df = new SimpleDateFormat("HH:mm");
		timeShow.setText(df.format(mCurrentDate.getTime()));
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
		setUIEnable(true);
		initTempFlowData();
		mFirst = findViewById(R.id.first_main_ui_id);
		mScond = findViewById(R.id.second_main_ui_id);
		dismissAnima(mFirst);
	}
	
	public void modelText(View v){
		initEditModel();
		mModelEditPop.showAtLocation(v, Gravity.TOP, 65, 150);
		Window win = getWindow();
		LayoutParams lp = win.getAttributes();
		lp.dimAmount = 0.5f;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
				
				int month = mCurrentDate.get(Calendar.MONTH) + 1;
				if (month > 2 && month <=5){
					mBackground.setBackgroundResource(R.drawable.beijing_chun);
				} else if (month > 5 && month <= 8){
					mBackground.setBackgroundResource(R.drawable.beijing_xia);
				} else if (month > 8 && month <= 11) {
					mBackground.setBackgroundResource(R.drawable.beijing_qiu);
				} else {
					mBackground.setBackgroundResource(R.drawable.beijing_dong);
				}
				displayAnima(mScond);
			}
		});
		v0.startAnimation(anim);
	}
	
	public void changeSkin(Calendar cal){
		int month = cal.get(Calendar.MONTH) + 1;
		if (month > 2 && month <=5){
			mController.setSkin(SkinController.CHUN);
		} else if (month > 5 && month <= 8){
			mController.setSkin(SkinController.XIA);
		} else if (month > 8 && month <= 11) {
			mController.setSkin(SkinController.QIU);
		} else {
			mController.setSkin(SkinController.DONG);
		}
		handleTemplature(0);
	}
	
	public void gotoMusic(View v){
		Intent intent = new Intent(this,MusicActivity.class);
		startActivity(intent);
	}
	
	public void onSave(View v){
		int current = mPager.getCurrentItem();
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		sp.edit().putInt(PREF_TEMP_SAVE_KEY+current, mTemplature)
			.putInt(PREF_FLOW_SAVE_KEY+current, mFlowRate).apply();
		Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
	}
	
	//TODO hide virtual key
	public static void enterLightsOutMode(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);
    }
}
