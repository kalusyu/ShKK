package com.shower;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

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

	public SkinController(MainActivity context) {
		mCtx = context;
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

	private void init3(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_dong;
		skin.mBeijing = R.drawable.beijing_dong;
		
		
		skin.tempNumberName = "wendushuzi%d_dong_normal";
		skin.sheshidu = R.drawable.sheshidu_dong_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_dong;
		skin.templatureReduce = R.drawable.wendu_r_btn_dong;
	}

	private void init2(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_qiu;
		skin.mBeijing = R.drawable.beijing_qiu;
		
		skin.tempNumberName = "wendushuzi%d_qiu_normal";
		skin.sheshidu = R.drawable.sheshidu_qiu_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_qiu;
		skin.templatureReduce = R.drawable.wendu_r_btn_qiu;
	}

	private void init1(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_xia;
		skin.mBeijing = R.drawable.beijing_xia;
		
		skin.tempNumberName = "wendushuzi%d_xia_normal";
		skin.sheshidu = R.drawable.sheshidu_xia_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn_xia;
		skin.templatureReduce = R.drawable.wendu_r_btn_xia;
	}

	private void init0(Skin skin) {
		skin.mBeijingModel = R.drawable.beijing_model_chun;
		skin.mBeijing = R.drawable.beijing_chun;
		skin.tempNumberName = "wendushuzi%d_chun_normal";
		skin.sheshidu = R.drawable.sheshidu_chun_normal;
		skin.templaturePlus = R.drawable.wendu_p_btn;
		skin.templatureReduce = R.drawable.wendu_r_btn;
	}

	public void setSkin(int type) {
		View v = mCtx.findViewById(R.id.main_bg);
		View sheshidu = mCtx.findViewById(R.id.templature_du);
		ImageView tempReduce = (ImageView)mCtx.findViewById(R.id.templature_reduce);
		ImageView tempPlus = (ImageView)mCtx.findViewById(R.id.templature_plus);
		switch (type) {
		case CHUN:
			v.setBackgroundResource(mSkin[0].mBeijingModel);
			tempDrawableName = mSkin[0].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[0].sheshidu);
			tempReduce.setImageResource(mSkin[0].templatureReduce);
			tempPlus.setImageResource(mSkin[0].templaturePlus);
			break;
		case XIA:
			v.setBackgroundResource(mSkin[1].mBeijingModel);
			tempDrawableName = mSkin[1].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[1].sheshidu);
			tempReduce.setImageResource(mSkin[1].templatureReduce);
			tempPlus.setImageResource(mSkin[1].templaturePlus);
			break;

		case QIU:
			v.setBackgroundResource(mSkin[2].mBeijingModel);
			tempDrawableName = mSkin[2].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[2].sheshidu);
			tempReduce.setImageResource(mSkin[2].templatureReduce);
			tempPlus.setImageResource(mSkin[2].templaturePlus);
			break;
		case DONG:
			v.setBackgroundResource(mSkin[3].mBeijingModel);
			tempDrawableName = mSkin[3].tempNumberName;
			sheshidu.setBackgroundResource(mSkin[3].sheshidu);
			tempReduce.setImageResource(mSkin[3].templatureReduce);
			tempPlus.setImageResource(mSkin[0].templaturePlus);
			break;

		default:
			break;
		}
	}
}
