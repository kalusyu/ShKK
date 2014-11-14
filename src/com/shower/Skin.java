package com.shower;

import android.content.Context;
import android.content.res.Resources;

public class Skin {

	Context mContext;
	Resources mResources;

	int mBeijing;
	int mBeijingModel;
	int[] shower = new int[4];
	
	// temperature
	int templatureReduce, templaturePlus;
	String tempNumberName;
	int sheshidu;
	
	int tempD;
	int flowReduce, flowPlus;
	int[] flowScrollBar = new int[3];
	
	// shower buttons
	int cepen,dingpen,pubu,shouchi;
	int cepenon,dingpenon,pubuon,shouchion;

	public Skin(Context context) {
		mContext = context;
		mResources = context.getResources();
	}
}
