package com.shower.ui;


import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ModelPagerAdapter extends PagerAdapter{

	int count;
	Context mContext;
	SparseArray<Model> mSparse;
	
	public ModelPagerAdapter(Context ctx,int size) {
		count = size;
		mContext = ctx;
	}
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	 @Override
     public Object instantiateItem(ViewGroup container, int position) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.model_item, null);
         TextView tv = (TextView)view.findViewById(R.id.model_text);
         Model model = mSparse.get(position);
         tv.setText(model.modelName);
         ImageView iv = (ImageView)view.findViewById(R.id.model_photo);
         iv.setImageResource(model.modelResource);
         container.addView(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
         return view;
     }
     
     @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView((View) object);  
     }
     
     public void setData(SparseArray<Model> s){
    	 mSparse = s;
     }

}
