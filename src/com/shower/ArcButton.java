package com.shower;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ArcButton extends ImageView{

	Bitmap mBitmap;
	public ArcButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int color = mBitmap
                    .getPixel((int) event.getX(), (int) event.getY());
            if (color == 0) {
                return false;
            }
        }
		return super.onTouchEvent(event);
	}
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 && h == 0 && oldw == 0 && oldh == 0) {
            super.onSizeChanged(w, h, oldw, oldh);
        } else {
            final StateListDrawable bkg = (StateListDrawable) getDrawable();
            mBitmap = Bitmap.createScaledBitmap(
                    ((BitmapDrawable) bkg.getCurrent()).getBitmap(),
                    getWidth(), getHeight(), true);
        }
    }

	
}
