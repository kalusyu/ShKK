package com.shower;

import android.content.Context;
import android.content.res.Resources;

public class Skin {

	private Context mContext;
	private Resources mResources;
	
	private int mBeijing;
	private int[] shower = new int[4];
	private int templatureReduce,templaturePlus;
	private int tempTen,tempUnic,tempS;
	private int flowReduce,flowPlus;
	private int[] flowScrollBar = new int[3];
	
	public Skin(Context context) {
		mContext = context;
		mResources = context.getResources();
	}
}
