package com.shower.ui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.shower.ui.SkinController.SkinCallbacks;

public class MainActivity extends Activity implements SkinCallbacks,OnSeekBarChangeListener{

	public static final String PREF_SAVE = "com.shower.prefer.save";
	public static final String PREF_DATE_SAVE_KEY = "pref_date_save_key";
	public static final String PREF_MODEL_SAVE_KEY = "pref_model_save_key";
	public static final String PREF_TEMP_SAVE_KEY = "pref_temp_save_key";
	public static final String PREF_FLOW_SAVE_KEY = "pref_flow_save_key";
	public static final String PREF_FLOW_SEEK_SAVE_KEY = "pref_flow_seek_save_key";
	public static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_MODEL_DATA = "自定义（五）:女;常规模式:男;常规模式:女;常规模式:男;自定义（一）:女;";
	public static final String PREF_COLOR_SAVE_KEY = "pref_color_save_key";
	public static final String PREF_COLOR_CENTER_SAVE_KEY = "pref_color_center_save_key";
	
	
	public static final int SEEK_VAR = 33;
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
	ImageView addYear;
	ImageView reduceYear;
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
	TextView mYearHanzi,mMonthHanzi,mDayHanzi;
	
	int mCurrentModelPage;
	
	PopupWindow mColorPop;
	
	PopupWindow mMusicPop;
	
	int mCurrentColorRes;
	int mCurrentColorCenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mShower = new ShowerImpl(this);
		mController = new SkinController(this);
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		enterLightsOutMode(getWindow());
		super.onCreate(savedInstanceState);
		
		// 1024 * 600
		setContentView(R.layout.main);
		initUI();
		initWindowSize();
		initMusicData();
		
		// hide virtual key
		
//		mBackground.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}
	
	private void initMusicData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "Music" ;
				search(new File(dir),new String[]{"mp3"});
			}
		}).start();
	}
	
	private void search(File file, String[] ext) {  
        if (file != null) {  
            if (file.isDirectory()) {  
                File[] listFile = file.listFiles();  
                if (listFile != null) {  
                    for (int i = 0; i < listFile.length; i++) {  
                        search(listFile[i], ext);  
                    }  
                }  
            } else {  
                String filename = file.getAbsolutePath();  
                for (int i = 0; i < ext.length; i++) {  
                    if (filename.endsWith(ext[i])) {  
                        mLists.add(filename);  
                        break;  
                    }  
                }  
            }  
        }  
    }  

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	}
	
	public void fullScreen(View v){
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		mBackground.requestFocus();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mFirst = findViewById(R.id.first_main_ui_id);
		mScond = findViewById(R.id.second_main_ui_id);
		mFirst.setVisibility(View.VISIBLE);
		mScond.setVisibility(View.GONE);
		setUIEnable(false);
		if (mDatetimePicker.isShowing()){
			mDatetimePicker.dismiss();
		}
		if (mColorPop.isShowing()){
			mColorPop.dismiss();
		}
		if (mMusicPop.isShowing()){
			mMusicPop.dismiss();
		}
		mController.isModel = true;
		changeSkin(mCurrentDate);
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		mCurrentColorRes = sp.getInt(PREF_COLOR_SAVE_KEY, mShower.getDefaultColor());
		mCurrentColorCenter = sp.getInt(PREF_COLOR_CENTER_SAVE_KEY, R.drawable.yansexianshi_caolv_normal);
		changeSeekbarSkin(mColorSeekBar, mCurrentColorRes);//TODO
		mImageColor.setImageResource(mCurrentColorCenter);
		changeMusicSeekbar(mMusicSoundSeekBar);
	}
	

	private void changeMusicSeekbar(VerticalSeekBar musicSoundSeekBar) {
		LayerDrawable layerDrawable = (LayerDrawable)seekbar.getProgressDrawable();
		Drawable[] out = new Drawable[layerDrawable.getNumberOfLayers()];
		for (int i=0,size=out.length; i < size; i++){
			switch(layerDrawable.getId(i)){
			case android.R.id.background:
				out[i] = getResources().getDrawable(R.drawable.dengguangqiangruokuang_normal);
				break;
			case android.R.id.secondaryProgress:
				out[i] = getResources().getDrawable(R.drawable.dengguangqiangruokuang_normal);
				break;
			case android.R.id.progress:
				Drawable drawable = getResources().getDrawable(R.drawable.yinliangtiao_normal);
				ClipDrawable oidDrawable = (ClipDrawable) layerDrawable
						.getDrawable(i);
				ClipDrawable proDrawable = new ClipDrawable(drawable,Gravity.LEFT, ClipDrawable.HORIZONTAL);
				proDrawable.setLevel(oidDrawable.getLevel());
				out[i] = proDrawable;
				break;
				default:
					break;
			}
		}
		if (out[0] != null && out[1] != null && out[2] != null){
			LayerDrawable l = new LayerDrawable(out);
			l.setId(0, android.R.id.background);
			l.setId(1, android.R.id.secondaryProgress);
			l.setId(2, android.R.id.progress);
			musicSoundSeekBar.setProgressDrawable(l);
			musicSoundSeekBar.setThumb(getResources().getDrawable(R.drawable.dengguangqiangruoanniu_normal));
			musicSoundSeekBar.setThumbOffset(0);
		} 
	}

	private void setUIEnable(boolean b) {
		View date = findViewById(R.id.main_datetime_text_show);
		View color = findViewById(R.id.main_color_show);
		View music = findViewById(R.id.main_music_show);
		View lanya = findViewById(R.id.main_lanya_show);
		View save = findViewById(R.id.on_save);
		date.setEnabled(b);
		color.setEnabled(b);
		music.setEnabled(b);
		lanya.setEnabled(b);
		save.setEnabled(b);
	}

	private void initTempFlowData() {
		int current = mPager.getCurrentItem();
		mCurrentModelPage = current;
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		int temp = sp.getInt(PREF_TEMP_SAVE_KEY+current, mShower.getTempalture());//mShower.getTempalture() default templature
		setCurrentTemplatrue(temp);
		setTemplatureTag(0);
		handleTemplature(0);
		// useless
		int flow = sp.getInt(PREF_FLOW_SAVE_KEY+current, mShower.getFlow());
		setCurrentFlow(flow); 
		setFlowTag(0);
		handleFlow(flow);
		// instead of above
		newProgress = sp.getInt(PREF_FLOW_SEEK_SAVE_KEY + current, mShower.getFlow());
		handSeekBarProgress(seekbar);
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
				String.format(mController.flowDrawableName, i), "drawable",
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
		initLiuliang();
		
		initColorPop();
		initMusicPop();
	}
	
	private void initMusicPop() {
		View dateView = getLayoutInflater().inflate(R.layout.music_main_ui_layout, null);
		initMusicUI(dateView);
		mMusicPop = new PopupWindow(dateView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mMusicPop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dengbiaoyanse_bai_normal)); 
		mMusicPop.getBackground().setAlpha(0);
		mMusicPop.setOutsideTouchable(false); // set false

		// use system animation
		mMusicPop.setAnimationStyle(android.R.style.Animation_Dialog);
		mMusicPop.update();
		mMusicPop.setTouchable(true);
		mMusicPop.setFocusable(true);
		
		mMusicPop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
				rl.removeViewAt(rl.getChildCount() - 1);;
				saveMusic();
			}

			private void saveMusic() {
				// TODO
//				String date = new SimpleDateFormat(FORMATTER).format(mCurrentDate.getTime());
//				SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
//				sp.edit().putString(PREF_DATE_SAVE_KEY, date).apply();
			}
		});
	}


	// Music
	TextView mTextMusicTitle,mTextMusicAuthor;
	MediaPlayer mPlayer = new MediaPlayer();
	ArrayList<String> mLists = new ArrayList<String>();
	private int currIndex = 0;
	VerticalSeekBar mMusicSoundSeekBar;
	private void initMusicUI(View dateView) {
		mTextMusicTitle = (TextView) dateView.findViewById(R.id.music_title);
		mTextMusicAuthor = (TextView) dateView.findViewById(R.id.music_author);
		mMusicSoundSeekBar = (VerticalSeekBar) dateView.findViewById(R.id.music_seek_bar);
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mMusicSoundSeekBar.setMax(maxVolume);
		mMusicSoundSeekBar.setProgress(currentVolume);
		mMusicSoundSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);    
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值  
                mMusicSoundSeekBar.setProgress(currentVolume);    
			}
		});
	}
	
	private boolean checkLoadedMusicData(){
		return mLists != null && mLists.size() > 0;
	}
	
	public void onNext(View v){
		if (checkLoadedMusicData()){
		
			if(currIndex+1 < mLists.size()){  
	            currIndex++;  
	            start();  
	        }else{  
	            Toast.makeText(this, "当前已经是最后一首歌曲了", Toast.LENGTH_SHORT).show();  
	        }  
		}
	}
	
	public void onPre(View v){
		if (checkLoadedMusicData()){
			if((currIndex-1)>=0){  
	            currIndex--;  
	            start();  
	        }else{  
	            Toast.makeText(this, "当前已经是第一首歌曲了", Toast.LENGTH_SHORT).show();  
	        } 
		}
	}
	
	private void start() {  
        if (mLists.size() > 0 && currIndex < mLists.size()) {  
            String songPath = mLists.get(currIndex);
            Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.TITLE,Media.ARTIST} 
            				,"_data=?", new String[]{songPath}, null);
            
            int titleIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            cursor.moveToFirst();
            mTextMusicTitle.setText(cursor.getString(titleIndex));
            mTextMusicAuthor.setText(cursor.getString(artistIndex));
            mPlayer.reset();  
            try {  
                mPlayer.setDataSource(songPath);  
                mPlayer.prepare();  
                mPlayer.start();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }else{  
            Toast.makeText(this, "播放列表为空", Toast.LENGTH_SHORT).show();  
        }  
    }  
	
	public void startStop(View v){
		ImageView view = (ImageView) v;
		if (view.getTag() == null){
			view.setTag(ON);
			view.setImageResource(R.drawable.bofang_btn_on);
			start();
		} else {
			Integer status = (Integer) view.getTag();
			if (status == ON){
				view.setTag(OFF);
				view.setImageResource(R.drawable.bofang_btn);
				mPlayer.stop();
			} else if (status == OFF){
				view.setTag(ON);
				view.setImageResource(R.drawable.bofang_btn_on);
				start();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
	}

	private void initColorPop() {
		View dateView = getLayoutInflater().inflate(R.layout.color_main_ui_layout, null);
		initColorUI(dateView);
		mColorPop = new PopupWindow(dateView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mColorPop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dengbiaoyanse_bai_normal)); 
		mColorPop.getBackground().setAlpha(0);
		mColorPop.setOutsideTouchable(false); // set false

		// use system animation
		mColorPop.setAnimationStyle(android.R.style.Animation_Dialog);
		mColorPop.update();
		mColorPop.setTouchable(true);
		mColorPop.setFocusable(true);
		
		mColorPop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
				rl.removeViewAt(rl.getChildCount() - 1);;
				saveColor();
			}

			private void saveColor() {
				// TODO
//				String date = new SimpleDateFormat(FORMATTER).format(mCurrentDate.getTime());
				SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
				sp.edit().putInt(PREF_COLOR_SAVE_KEY, mCurrentColorRes).putInt(PREF_COLOR_CENTER_SAVE_KEY, mCurrentColorCenter).apply();
			}
		});
	}
	
	private  int mCurrentColorHight;
	private int mOldProgrees;

	private void initColorUI(View dateView) {
		mImageColor = (ImageView) dateView.findViewById(R.id.center);
		mColorSeekBar = (VerticalSeekBar) dateView.findViewById(R.id.color_seek_bar);
		mColorSeekBar.setMax(100);
		mColorSeekBar.setProgress(0);
		mColorSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			private int lastProgress = 0;
			private int newProgress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress > newProgress + 100 || progress < newProgress - 100){
					newProgress = lastProgress;
					seekBar.setProgress(lastProgress);
					return;
				}
				mCurrentColorHight = newProgress = progress;
			}
			
		

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handSeekBarProgress(seekBar);
			}
			
			public void handSeekBarProgress(SeekBar seekBar){
				if (newProgress < 25) {
					lastProgress = 0;
					newProgress = 0;
					seekBar.setProgress(0);
				} else if (newProgress >= 25 && newProgress < 70) {
					lastProgress = 50;
					newProgress = 50;
					seekBar.setProgress(50);
				}  else {
					lastProgress=100;
		            newProgress=100;
		            seekBar.setProgress(100);
				}
			}
		});
	}

	private void initLiuliang() {
		seekbar = (SeekBar) findViewById(R.id.seek_bar);

		// 设置初值
		seekbar.setMax(100);
		seekbar.setProgress(0);
		seekbar.setOnSeekBarChangeListener(this);
	}

	private void initEditModel() {
		View dateView = getLayoutInflater().inflate(R.layout.model_edit_ui_layout, null);
		initEditModelUI(dateView);
		mModelEditPop = new PopupWindow(dateView,
				288,
				233);
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
		mModelName = model.modelName;
		modelEdit.setText(mModelName);
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
				// logic maybe some errors
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
			String men = custom[1];
			model.modelResource = "男".equals(men) ? R.drawable.men_normal:R.drawable.women_normal;
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
		newProgress = lastProgress - SEEK_VAR;
		handSeekBarProgress(seekbar);
	}

	public void flowPlus(View v) {
		if (setFlowTag(1)) {
			handleFlow(mFlowRate);
			mShower.addFlow(1);
		}
		newProgress = lastProgress + SEEK_VAR;
		handSeekBarProgress(seekbar);
		
	}

	// shower button
	public void cepeng(View v) {
		ImageView img = (ImageView) v;
		handShowerButton(img,CEPENG);
	}

	public void dingpeng(View v) {
		ImageView img = (ImageView) v;
		handShowerButton(img,DINGPENG);
	}

	public void pubu(View v) {
		ImageView img = (ImageView) v;
		handShowerButton(img,PUBU);
	}

	public void shouchi(View v) {
		ImageView img = (ImageView) v;
		handShowerButton(img,SHOUCHI);
	}

	public void zhongjian(View v) {
		ImageView img = (ImageView) v;
		handShower(img, LAUNCH,R.drawable.zhongjian_btn,R.drawable.zhongjian_btn_on);
	}
	
	public void setShowerButtonState(int status){
		ImageView v = (ImageView) findViewById(R.id.center);
		if (status == ON){
			v.setTag(ON);
			v.setImageResource(R.drawable.zhongjian_btn_on);
		} else if (status == OFF){
			v.setTag(OFF);
			v.setImageResource(R.drawable.zhongjian_btn);
		} else {
			// TODO other things
		}
	}
	
	public void handShowerButton(ImageView img,int type){
		switch (mController.mCurrSeason) {
		case SkinController.CHUN:
		case SkinController.QIU:
			if (type == CEPENG ){
				handShower(img,type, R.drawable.cepeng_btn,R.drawable.cepeng_btn_on);
			} else if (type == DINGPENG){
				handShower(img,type, R.drawable.dingpeng_btn,R.drawable.dingpeng_btn_on);
			} else if (type == PUBU){
				handShower(img,type, R.drawable.pubu_btn,R.drawable.pubu_btn_on);
			} else {
				handShower(img,type, R.drawable.shouchi_btn,R.drawable.shouchi_btn_on);
			}
			break;
		case SkinController.XIA:
			if (type == CEPENG ){
				handShower(img,type, R.drawable.cepeng_xia_btn,R.drawable.cepeng_xia_btn_on);
			} else if (type == DINGPENG){
				handShower(img,type, R.drawable.dingpeng_xia_btn,R.drawable.dingpeng_xia_btn_on);
			} else if (type == PUBU){
				handShower(img,type, R.drawable.pubu_xia_btn,R.drawable.pubu_xia_btn_on);
			} else {
				handShower(img,type, R.drawable.shouchi_xia_btn,R.drawable.shouchi_xia_btn_on);
			}
			break;
		case SkinController.DONG:
			if (type == CEPENG ){
				handShower(img,type, R.drawable.cepeng_dong_btn,R.drawable.cepeng_dong_btn_on);
			} else if (type == DINGPENG){
				handShower(img,type, R.drawable.dingpeng_dong_btn,R.drawable.dingpeng_dong_btn_on);
			} else if (type == PUBU){
				handShower(img,type, R.drawable.pubu_dong_btn,R.drawable.pubu_dong_btn_on);
			} else {
				handShower(img,type, R.drawable.shouchi_dong_btn,R.drawable.shouchi_dong_btn_on);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void handShower(ImageView img, int type, int resourceNormal,int resourceOn) {
		Object tag = img.getTag();
		if (tag == null) {
			img.setTag(OFF);
			img.setImageResource(resourceNormal);
			switch (type) {
			case CEPENG:
				mShower.cepengOff();
				break;
			case DINGPENG:
				mShower.dingpengOff();
				break;
			case PUBU:
				mShower.pubuOff();
				break;
			case SHOUCHI:
				mShower.shouchiOff();
				break;
			case LAUNCH:
				img.setTag(ON);
				img.setImageResource(resourceOn);
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
		mOldTemplature = mTemplature;
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
		Bitmap bmp = ShowerUtils.blurBitmap(ShowerUtils.getScreenShortcut(this,mWindowWidth,mWindowHeight),this);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(bmp);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
		rl.addView(iv);
		mDatetimePicker.showAtLocation(v, Gravity.RIGHT | Gravity.BOTTOM, 0, 100);
	}
	
	public void musicClick(View v){
		Bitmap bmp = ShowerUtils.blurBitmap(ShowerUtils.getScreenShortcut(this,mWindowWidth,mWindowHeight),this);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(bmp);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
		rl.addView(iv);
		mMusicPop.showAtLocation(v, Gravity.CENTER | Gravity.BOTTOM, 0, 100);
	}
	
	public void colorClick(View v){
		Bitmap bmp = ShowerUtils.blurBitmap(ShowerUtils.getScreenShortcut(this,mWindowWidth,mWindowHeight),this);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(bmp);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
		rl.addView(iv);
		mColorPop.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0, 100);
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
		mDatetimePicker.setBackgroundDrawable(new ColorDrawable(0x55000000)); 
//		mDatetimePicker.getBackground().setAlpha((int)(255*0.7));
		mDatetimePicker.setOutsideTouchable(true);

		// use system animation
		mDatetimePicker.setAnimationStyle(android.R.style.Animation_Dialog);
		mDatetimePicker.update();
		mDatetimePicker.setTouchable(true);
		mDatetimePicker.setFocusable(true);

		mDatetimePicker.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_bg);
				rl.removeViewAt(rl.getChildCount() - 1);;
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
	
	public void addYear(View v) {
		int oldVal = mCurrentDate.get(Calendar.YEAR);
		int newVal = oldVal + 1;
		onChangeYear(oldVal, newVal);
	}
	
	public void reduceYear(View v) {
		int oldVal = mCurrentDate.get(Calendar.YEAR);
		int newVal = oldVal - 1;
		onChangeYear(oldVal, newVal);
	}
	
	public void onChangeYear(int oldVal, int newVal){
        mCurrentDate.add(Calendar.YEAR, newVal - oldVal);
		notifyDateChanged();
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
		timeShow.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Arial.ttf"));
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
		addYear = (ImageView)dateView.findViewById(R.id.addYear);
		reduceYear = (ImageView)dateView.findViewById(R.id.reduceYear);
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
		
		mYearHanzi = (TextView) dateView.findViewById(R.id.year_hanzi);
		mMonthHanzi = (TextView) dateView.findViewById(R.id.month_hanzi);
		mDayHanzi = (TextView) dateView.findViewById(R.id.day_hanzi);
		
	}

	public static final int SHOWER_YEAR = -105;
	public static final int SHOWER_MONTH = -101;
	public static final int SHOWER_DAY = -102;
	public static final int SHOWER_HOUR = -103;
	public static final int SHOWER_MINUTE = -104;
	public void yearOnClick(View v) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.datetime_picker_year,options);
		
		int inSample = 1;
		if (options.outWidth > 467 && options.outHeight > 255){
			inSample = Math.max((int)Math.ceil((float)options.outWidth/467), (int)Math.ceil((float)options.outHeight / 255));
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = inSample;
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.datetime_picker_year, options);
		
		
		mDateTimePickerBg.setBackground(new BitmapDrawable(getResources(), bmp));
		setAddReduceVisible(SHOWER_YEAR);
	}
	
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
		case SHOWER_YEAR:
			addYear.setVisibility(View.VISIBLE);
			reduceYear.setVisibility(View.VISIBLE);
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			mYearText.setTextColor(Color.parseColor("#3bb1fe"));
			mYearHanzi.setTextColor(Color.parseColor("#3bb1fe"));
			mMonthText.setTextColor(Color.parseColor("#ffffff"));
			mMonthHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayText.setTextColor(Color.parseColor("#ffffff"));
			mHourText.setTextColor(Color.parseColor("#ffffff"));
			mMinuteText.setTextColor(Color.parseColor("#ffffff"));
			break;
		case SHOWER_MONTH:
			addYear.setVisibility(View.GONE);
			reduceYear.setVisibility(View.GONE);
			addMonth.setVisibility(View.VISIBLE);
			reduceMonth.setVisibility(View.VISIBLE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			mYearText.setTextColor(Color.parseColor("#ffffff"));
			mYearHanzi.setTextColor(Color.parseColor("#ffffff"));
			mMonthText.setTextColor(Color.parseColor("#3bb1fe"));
			mMonthHanzi.setTextColor(Color.parseColor("#3bb1fe"));
			mDayHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayText.setTextColor(Color.parseColor("#ffffff"));
			mHourText.setTextColor(Color.parseColor("#ffffff"));
			mMinuteText.setTextColor(Color.parseColor("#ffffff"));
			
			break;
		case SHOWER_DAY:
			addYear.setVisibility(View.GONE);
			reduceYear.setVisibility(View.GONE);
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.VISIBLE);
			reduceDay.setVisibility(View.VISIBLE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			mYearText.setTextColor(Color.parseColor("#ffffff"));
			mYearHanzi.setTextColor(Color.parseColor("#ffffff"));
			mMonthText.setTextColor(Color.parseColor("#ffffff"));
			mDayText.setTextColor(Color.parseColor("#3bb1fe"));
			mMonthHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayHanzi.setTextColor(Color.parseColor("#3bb1fe"));
			mHourText.setTextColor(Color.parseColor("#ffffff"));
			mMinuteText.setTextColor(Color.parseColor("#ffffff"));
			break;
		case SHOWER_HOUR:
			addYear.setVisibility(View.GONE);
			reduceYear.setVisibility(View.GONE);
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.VISIBLE);
			reduceHour.setVisibility(View.VISIBLE);
			addMinute.setVisibility(View.GONE);
			reduceMinute.setVisibility(View.GONE);
			mYearText.setTextColor(Color.parseColor("#ffffff"));
			mYearHanzi.setTextColor(Color.parseColor("#ffffff"));
			mMonthText.setTextColor(Color.parseColor("#ffffff"));
			mDayText.setTextColor(Color.parseColor("#ffffff"));
			mHourText.setTextColor(Color.parseColor("#3bb1fe"));
			mMinuteText.setTextColor(Color.parseColor("#ffffff"));
			mMonthHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayHanzi.setTextColor(Color.parseColor("#ffffff"));
			break;
		case SHOWER_MINUTE:
			addYear.setVisibility(View.GONE);
			reduceYear.setVisibility(View.GONE);
			addMonth.setVisibility(View.GONE);
			reduceMonth.setVisibility(View.GONE);
			addDay.setVisibility(View.GONE);
			reduceDay.setVisibility(View.GONE);
			addHour.setVisibility(View.GONE);
			reduceHour.setVisibility(View.GONE);
			addMinute.setVisibility(View.VISIBLE);
			reduceMinute.setVisibility(View.VISIBLE);
			mYearText.setTextColor(Color.parseColor("#ffffff"));
			mYearHanzi.setTextColor(Color.parseColor("#ffffff"));
			mMonthText.setTextColor(Color.parseColor("#ffffff"));
			mDayText.setTextColor(Color.parseColor("#ffffff"));
			mHourText.setTextColor(Color.parseColor("#ffffff"));
			mMinuteText.setTextColor(Color.parseColor("#3bb1fe"));
			mMonthHanzi.setTextColor(Color.parseColor("#ffffff"));
			mDayHanzi.setTextColor(Color.parseColor("#ffffff"));
			break;
			default:
				addYear.setVisibility(View.GONE);
				reduceYear.setVisibility(View.GONE);
				addMonth.setVisibility(View.GONE);
				reduceMonth.setVisibility(View.GONE);
				addDay.setVisibility(View.GONE);
				reduceDay.setVisibility(View.GONE);
				addHour.setVisibility(View.GONE);
				reduceHour.setVisibility(View.GONE);
				addMinute.setVisibility(View.GONE);
				reduceMinute.setVisibility(View.GONE);
				mYearText.setTextColor(Color.parseColor("#ffffff"));
				mYearHanzi.setTextColor(Color.parseColor("#ffffff"));
				mMonthText.setTextColor(Color.parseColor("#ffffff"));
				mDayText.setTextColor(Color.parseColor("#ffffff"));
				mHourText.setTextColor(Color.parseColor("#ffffff"));
				mMinuteText.setTextColor(Color.parseColor("#ffffff"));
				mMonthHanzi.setTextColor(Color.parseColor("#ffffff"));
				mDayHanzi.setTextColor(Color.parseColor("#ffffff"));
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
		mModelEditPop.showAtLocation((View)v.getParent(), Gravity.CENTER_HORIZONTAL, 0, -160);
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
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		int flow = sp.getInt(PREF_FLOW_SAVE_KEY+mCurrentModelPage, mShower.getFlow());
		handleFlow(flow);
	}
	
	public void gotoMusic(View v){
		Intent intent = new Intent(this,MusicActivity.class);
		intent.putExtra("type", mController.mCurrSeason);
		startActivity(intent);
	}
	
	public void onSave(View v){
		int current = mPager.getCurrentItem();
		SharedPreferences sp = getSharedPreferences(PREF_SAVE, Context.MODE_PRIVATE);
		sp.edit().putInt(PREF_TEMP_SAVE_KEY+current, mTemplature)
			.putInt(PREF_FLOW_SAVE_KEY+current, mFlowRate)
			.putInt(PREF_FLOW_SEEK_SAVE_KEY+current, newProgress).apply();
		Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
	}
	
	//TODO hide virtual key
	public static void enterLightsOutMode(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);
    }
	
	
	private SeekBar seekbar;

	private int lastProgress = 0;
	private int newProgress = 0;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress > newProgress + 100 || progress < newProgress - 100){
			newProgress = lastProgress;
			seekBar.setProgress(lastProgress);
			return;
		}
		newProgress = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		handSeekBarProgress(seekBar);
	}
	
	public void handSeekBarProgress(SeekBar seekBar){
		if (newProgress < 16) {
			lastProgress = 0;
			newProgress = 0;
			seekBar.setProgress(0);
		} else if (newProgress >= 16 && newProgress < 49) {
			lastProgress = 33;
			newProgress = 33;
			seekBar.setProgress(33);
		} else if (newProgress > 49 && newProgress <= 82) {
			lastProgress = 66;
			newProgress = 66;
			seekBar.setProgress(66);

		}  else {
			lastProgress=100;
            newProgress=100;
            seekBar.setProgress(100);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  {  
        	Intent i = new Intent(this,ResultActivity.class);
        	i.putExtra("tempD", mTemplature);
        	i.putExtra("timeD", mShower.getShowerTime());//TODO 需要填写使用时间 
        	i.putExtra("flowD", mShower.getShowerFlow());
        	startActivity(i);
        	this.finish();
        }  
        return true;  
    }  
	
	// color
	private ImageView mImageColor;
	private VerticalSeekBar mColorSeekBar;
	
	public void handColorUI(int centerRes,int seekbarResource){
		mCurrentColorCenter = centerRes;
		mImageColor.setImageResource(centerRes);
		changeSeekbarSkin(mColorSeekBar,seekbarResource);
	}
	
	public void changeSeekbarSkin(SeekBar seekbar,int typeResource){
		LayerDrawable layerDrawable = (LayerDrawable)seekbar.getProgressDrawable();
		Drawable[] out = new Drawable[layerDrawable.getNumberOfLayers()];
		for (int i=0,size=out.length; i < size; i++){
			switch(layerDrawable.getId(i)){
			case android.R.id.background:
				out[i] = getResources().getDrawable(R.drawable.dengguangqiangruokuang_normal);
				break;
			case android.R.id.secondaryProgress:
				out[i] = getResources().getDrawable(R.drawable.dengguangqiangruokuang_normal);
				break;
			case android.R.id.progress:
				Drawable drawable = getResources().getDrawable(typeResource);
				ClipDrawable oidDrawable = (ClipDrawable) layerDrawable
						.getDrawable(i);
				ClipDrawable proDrawable = new ClipDrawable(drawable,Gravity.LEFT, ClipDrawable.HORIZONTAL);
				proDrawable.setLevel(oidDrawable.getLevel());
				out[i] = proDrawable;
				break;
				default:
					break;
			}
		}
		if (out[0] != null && out[1] != null && out[2] != null){
			LayerDrawable l = new LayerDrawable(out);
			l.setId(0, android.R.id.background);
			l.setId(1, android.R.id.secondaryProgress);
			l.setId(2, android.R.id.progress);
			seekbar.setProgressDrawable(l);
			seekbar.setThumb(getResources().getDrawable(R.drawable.dengguangqiangruoanniu_normal));
			seekbar.setThumbOffset(0);
		} 
		
		mOldProgrees = mCurrentColorHight;
		MotionEvent e;
		  //nano
		e=MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 41, 840, 0);
		seekbar.onTouchEvent(e);
//		seekbar.setProgress(mOldProgrees);
	}
	
	public void shenlan(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_shenlan_normal;
		handColorUI(R.drawable.yansexianshi_shenlan_normal,mCurrentColorRes);
		mShower.setBulbColor("shenlan");
	}
	
	public void zise(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_zi_normal;
		handColorUI(R.drawable.yansexianshi_zi_normal,mCurrentColorRes);
		mShower.setBulbColor("zise");
	}
	
	public void fenhong(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_fenhong_normal;
		handColorUI(R.drawable.yansexianshi_fenhong_normal,mCurrentColorRes);
		mShower.setBulbColor("fenhong");
	}
	
	public void hongse(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_hong_normal;
		handColorUI(R.drawable.yansexianshi_hong_normal,mCurrentColorRes);
		mShower.setBulbColor("hongse");
	}
	public void juhong(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_juhong_normal;
		handColorUI(R.drawable.yansexianshi_juhong_normal,mCurrentColorRes);
		mShower.setBulbColor("juhong");
	}
	
	public void chengse(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_cheng_normal;
		handColorUI(R.drawable.yansexianshi_cheng_normal,mCurrentColorRes);
		mShower.setBulbColor("chengse");
	}
	public void tuhuang(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_tuhuang_normal;
		handColorUI(R.drawable.yansexianshi_tuhuang_normal,mCurrentColorRes);
		mShower.setBulbColor("tuhuang");
	}
	
	public void huangse(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_huang_normal;
		handColorUI(R.drawable.yansexianshi_huang_normal,mCurrentColorRes);
		mShower.setBulbColor("huangse");
	}
	public void caolvse(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_caolv__normal;
		handColorUI(R.drawable.yansexianshi_caolv_normal,mCurrentColorRes);
		mShower.setBulbColor("caolvse");
	}
	
	public void lvse(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_lv_normal;
		handColorUI(R.drawable.yansexianshi_lv_normal,mCurrentColorRes);
		mShower.setBulbColor("lvse");
	}
	
	public void hulan(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_hulan_normal;
		handColorUI(R.drawable.yansexianshi_hulan_normal,mCurrentColorRes);
		mShower.setBulbColor("hulan");
	}
	
	public void gulan(View v){
		mCurrentColorRes = R.drawable.dengguangqiangruotiaoshang_gulan_normal;
		handColorUI(R.drawable.yansexianshi_gulan_normal,mCurrentColorRes);
		mShower.setBulbColor("gulan");
	}

	public int getDefaultColor() {
		return R.drawable.dengguangqiangruoanniu_normal;
	}
}
