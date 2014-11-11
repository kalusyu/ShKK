package com.shower;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class SkinController {
	private Skin[] mSkin = new Skin[4];

	private int mFlowScrollBarKind;

	public static final int CHUN = 0;
	public static final int XIA = 1;
	public static final int QIU = 2;
	public static final int DONG = 3;
	MainActivity mCtx;
	Resources mRes;

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
	}

	private void init2(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_qiu;
		skin.mBeijing = R.drawable.beijing_qiu;
	}

	private void init1(Skin skin) {
		// TODO Auto-generated method stub
		skin.mBeijingModel = R.drawable.beijing_model_xia;
		skin.mBeijing = R.drawable.beijing_xia;
	}

	private void init0(Skin skin) {
		skin.mBeijingModel = R.drawable.beijing_model_chun;
		skin.mBeijing = R.drawable.beijing_chun;
		
	}

	public void setSkin(int type) {
		View v = mCtx.findViewById(R.id.main_bg);
		switch (type) {
		case CHUN: 
			v.setBackgroundResource(mSkin[0].mBeijingModel);
			break;
		case XIA:
			v.setBackgroundResource(mSkin[1].mBeijingModel);
			break;
			
		case QIU:
			v.setBackgroundResource(mSkin[2].mBeijingModel);
			break;
		case DONG:
			v.setBackgroundResource(mSkin[3].mBeijingModel);
			break;

		default:
			break;
		}
	}
}
