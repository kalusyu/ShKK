package com.shower;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class SkinController {
	private Skin[] mSkin = new Skin[4];

	private int mFlowScrollBarKind;

	public static final int CHUN = 0;
	public static final int XIA = 1;
	public static final int QIU = 2;
	public static final int DONG = 3;
	MainActivity mCtx;
	Resources mRes;
	String tempDrawableName;
	String flowDrawableName;

	SkinCallbacks mCallbacks;
	boolean isModel = true;
	
	int mCurrSeason  = CHUN;

	public interface SkinCallbacks{
		void handShower(ImageView view, int type, int resourceNormal, int reousrceOn);
	}
	
	public SkinController(MainActivity context) {
		mCtx = context;
		mCallbacks = (SkinCallbacks) context;
		initSkin(context);
	}

	private void initSkin(Context context) {
		mSkin[0] = new Skin(context);
		mSkin[1] = new Skin(context);
		mSkin[2] = new Skin(context);
		mSkin[3] = new Skin(context);
		init0(mSkin[0]);
		init1(mSkin[1]);
		init2(mSkin[2]);
		init3(mSkin[3]);
	}

	// Wenst
	private void init3(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_dong;
		skin.mBeijing = R.drawable.beijing_dong;
		
		
		skin.tempNumberName = "wendushuzi%d_dong_normal";
		skin.sheshidu = R.drawable.sheshidu_dong_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_dong;
		skin.templatureReduce = R.drawable.wendu_r_btn_dong;
		
		skin.cepen = R.drawable.cepeng_dong_btn;
		skin.cepenon = R.drawable.cepeng_dong_btn_on;
		skin.dingpen = R.drawable.dingpeng_dong_btn;
		skin.dingpenon = R.drawable.dingpeng_dong_btn_on;
		skin.pubu = R.drawable.pubu_dong_btn;
		skin.pubuon = R.drawable.pubu_dong_btn_on;
		skin.shouchi = R.drawable.shouchi_dong_btn;
		skin.shouchion = R.drawable.shouchi_dong_btn_on;
		
		skin.flowKuangName = "liuliangtiao%d_qiu_normal";
		skin.flowPlus = R.drawable.liuliang_p_btn_dong;
		skin.flowReduce = R.drawable.liuliang_r_btn_dong;
		skin.seekDrawable = R.drawable.liuliangtiao3_dong_normal;
	}

	// Aut
	private void init2(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_qiu;
		skin.mBeijing = R.drawable.beijing_qiu;
		
		skin.tempNumberName = "wendushuzi%d_qiu_normal";
		skin.sheshidu = R.drawable.sheshidu_qiu_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_qiu;
		skin.templatureReduce = R.drawable.wendu_r_btn_qiu;
		
		skin.cepen = R.drawable.cepeng_btn;
		skin.cepenon = R.drawable.cepeng_btn_on;
		skin.dingpen = R.drawable.dingpeng_btn;
		skin.dingpenon = R.drawable.dingpeng_btn_on;
		skin.pubu = R.drawable.pubu_btn;
		skin.pubuon = R.drawable.pubu_btn_on;
		skin.shouchi = R.drawable.shouchi_btn;
		skin.shouchion = R.drawable.shouchi_btn_on;
		
		skin.flowKuangName = "liuliangtiao%d_qiu_normal";
		skin.flowPlus = R.drawable.liuliang_p_btn_qiu;
		skin.flowReduce = R.drawable.liuliang_r_btn_qiu;
		skin.seekDrawable = R.drawable.liuliangtiao3_chun_normal;
	}

	// Summer
	private void init1(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_xia;
		skin.mBeijing = R.drawable.beijing_xia;
		
		skin.tempNumberName = "wendushuzi%d_xia_normal";
		skin.sheshidu = R.drawable.sheshidu_xia_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_xia;
		skin.templatureReduce = R.drawable.wendu_r_btn_xia;
		
		skin.cepen = R.drawable.cepeng_xia_btn;
		skin.cepenon = R.drawable.cepeng_xia_btn_on;
		skin.dingpen = R.drawable.dingpeng_xia_btn;
		skin.dingpenon = R.drawable.dingpeng_xia_btn_on;
		skin.pubu = R.drawable.pubu_xia_btn;
		skin.pubuon = R.drawable.pubu_xia_btn_on;
		skin.shouchi = R.drawable.shouchi_xia_btn;
		skin.shouchion = R.drawable.shouchi_xia_btn_on;
		
		skin.flowKuangName = "liuliangtiao%d_xia_normal";
		skin.flowPlus = R.drawable.liuliang_p_btn_xia;
		skin.flowReduce = R.drawable.liuliang_r_btn_xia;
		skin.seekDrawable = R.drawable.liuliangtiao3_xia_normal;
	}

	// Spring
	private void init0(Skin skin) {
		skin.mBeijingModel = R.drawable.beijing_model_chun;
		skin.mBeijing = R.drawable.beijing_chun;
		skin.tempNumberName = "wendushuzi%d_chun_normal";
		skin.sheshidu = R.drawable.sheshidu_chun_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn;
		skin.templatureReduce = R.drawable.wendu_r_btn;
		skin.cepen = R.drawable.cepeng_btn;
		skin.cepenon = R.drawable.cepeng_btn_on;
		skin.dingpen = R.drawable.dingpeng_btn;
		skin.dingpenon = R.drawable.dingpeng_btn_on;
		skin.pubu = R.drawable.pubu_btn;
		skin.pubuon = R.drawable.pubu_btn_on;
		skin.shouchi = R.drawable.shouchi_btn;
		skin.shouchion = R.drawable.shouchi_btn_on;
		
		skin.flowKuangName = "liuliangtiao%d_chun_normal";
		skin.flowPlus = R.drawable.liuliang_p_btn;
		skin.flowReduce = R.drawable.liuliang_r_btn;
		skin.seekDrawable = R.drawable.liuliangtiao3_chun_normal;
	}

	public void setSkin(int type) {
		mCurrSeason  = type;
		View v = mCtx.findViewById(R.id.main_bg);
		View sheshidu = mCtx.findViewById(R.id.templature_du);
		ImageView tempReduce = (ImageView)mCtx.findViewById(R.id.templature_reduce);
		ImageView tempPlus = (ImageView)mCtx.findViewById(R.id.templature_plus);
		
		ImageView flowReduce = (ImageView)mCtx.findViewById(R.id.flow_reduce);
		ImageView flowPlus = (ImageView)mCtx.findViewById(R.id.flow_plus);
		SeekBar seekbar = (SeekBar) mCtx.findViewById(R.id.seek_bar);
		
		ArcButton left = (ArcButton) mCtx.findViewById(R.id.left);
		ArcButton top = (ArcButton) mCtx.findViewById(R.id.top);
		ArcButton right = (ArcButton) mCtx.findViewById(R.id.right);
		ArcButton bottom = (ArcButton) mCtx.findViewById(R.id.bottom);
		left.setTag(null);
		top.setTag(null);
		right.setTag(null);
		bottom.setTag(null);
		LayerDrawable progressDrawable = null;
		
		switch (type) {
		case CHUN:
			setBackground(v, isModel,mSkin[0]);
			tempDrawableName = mSkin[0].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[0].sheshidu);
			tempReduce.setImageResource(mSkin[0].templatureReduce);
			tempPlus.setImageResource(mSkin[0].templaturePlus);
			
			flowDrawableName = mSkin[0].flowKuangName;
			flowReduce.setImageResource(mSkin[0].flowReduce);
			flowPlus.setImageResource(mSkin[0].flowPlus);
			changeSeekbarSkin(progressDrawable,seekbar,mSkin[0].seekDrawable);
			
			mCallbacks.handShower(left, MainActivity.CEPENG, mSkin[0].cepen,mSkin[0].cepenon);
			mCallbacks.handShower(top, MainActivity.DINGPENG, mSkin[0].dingpen,mSkin[0].dingpenon);
			mCallbacks.handShower(right, MainActivity.PUBU, mSkin[0].pubu,mSkin[0].pubuon);
			mCallbacks.handShower(bottom, MainActivity.SHOUCHI, mSkin[0].shouchi,mSkin[0].shouchion);
			break;
		case XIA:
			setBackground(v, isModel,mSkin[1]);
			tempDrawableName = mSkin[1].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[1].sheshidu);
			tempReduce.setImageResource(mSkin[1].templatureReduce);
			tempPlus.setImageResource(mSkin[1].templaturePlus);
			
			flowDrawableName = mSkin[1].flowKuangName;
			flowReduce.setImageResource(mSkin[1].flowReduce);
			flowPlus.setImageResource(mSkin[1].flowPlus);
			changeSeekbarSkin(progressDrawable,seekbar,mSkin[1].seekDrawable);			
			
			mCallbacks.handShower(left, MainActivity.CEPENG, mSkin[1].cepen,mSkin[1].cepenon);
			mCallbacks.handShower(top, MainActivity.DINGPENG, mSkin[1].dingpen,mSkin[1].dingpenon);
			mCallbacks.handShower(right, MainActivity.PUBU, mSkin[1].pubu,mSkin[1].pubuon);
			mCallbacks.handShower(bottom, MainActivity.SHOUCHI, mSkin[1].shouchi,mSkin[1].shouchion);
			break;

		case QIU:
			setBackground(v, isModel,mSkin[2]);
			tempDrawableName = mSkin[2].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[2].sheshidu);
			tempReduce.setImageResource(mSkin[2].templatureReduce);
			tempPlus.setImageResource(mSkin[2].templaturePlus);
			
			flowDrawableName = mSkin[2].flowKuangName;
			flowReduce.setImageResource(mSkin[2].flowReduce);
			flowPlus.setImageResource(mSkin[2].flowPlus);
			changeSeekbarSkin(progressDrawable,seekbar,mSkin[2].seekDrawable);
			
			mCallbacks.handShower(left, MainActivity.CEPENG, mSkin[2].cepen,mSkin[2].cepenon);
			mCallbacks.handShower(top, MainActivity.DINGPENG, mSkin[2].dingpen,mSkin[2].dingpenon);
			mCallbacks.handShower(right, MainActivity.PUBU, mSkin[2].pubu,mSkin[2].pubuon);
			mCallbacks.handShower(bottom, MainActivity.SHOUCHI, mSkin[2].shouchi,mSkin[2].shouchion);
			break;
		case DONG:
			setBackground(v, isModel,mSkin[3]);
			tempDrawableName = mSkin[3].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[3].sheshidu);
			tempReduce.setImageResource(mSkin[3].templatureReduce);
			tempPlus.setImageResource(mSkin[3].templaturePlus);
			
			flowDrawableName = mSkin[3].flowKuangName;
			flowReduce.setImageResource(mSkin[3].flowReduce);
			flowPlus.setImageResource(mSkin[3].flowPlus);
			changeSeekbarSkin(progressDrawable,seekbar,mSkin[3].seekDrawable);
			
			mCallbacks.handShower(left, MainActivity.CEPENG, mSkin[3].cepen,mSkin[3].cepenon);
			mCallbacks.handShower(top, MainActivity.DINGPENG, mSkin[3].dingpen,mSkin[3].dingpenon);
			mCallbacks.handShower(right, MainActivity.PUBU, mSkin[3].pubu,mSkin[3].pubuon);
			mCallbacks.handShower(bottom, MainActivity.SHOUCHI, mSkin[3].shouchi,mSkin[3].shouchion);
			break;

		default:
			break;
		}
	}
	
	public void changeSeekbarSkin(LayerDrawable progressDrawable, SeekBar seekbar,int typeResource){
		LayerDrawable layerDrawable = (LayerDrawable)seekbar.getProgressDrawable();
		Drawable[] out = new Drawable[layerDrawable.getNumberOfLayers()];
		for (int i=0,size=out.length; i < size; i++){
			switch(layerDrawable.getId(i)){
			case android.R.id.background:
				out[i] = mCtx.getResources().getDrawable(R.drawable.liuliangkuang_normal);
				break;
			case android.R.id.secondaryProgress:
				out[i] = mCtx.getResources().getDrawable(R.drawable.liuliangkuang_normal);
				break;
			case android.R.id.progress:
				Drawable drawable = mCtx.getResources().getDrawable(typeResource);
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
			progressDrawable = new LayerDrawable(out);
			seekbar.setProgressDrawable(progressDrawable);
		}
	}
	
	private void setBackground(View v, boolean isModel, Skin skin) {
		if (isModel){
			v.setBackgroundResource(skin.mBeijingModel);
		} else {
			v.setBackgroundResource(skin.mBeijing);
		}		
	}
}
