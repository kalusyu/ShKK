package com.shower.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class ShowerTextView extends TextView{

	public ShowerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onDraw(Canvas canvas) {
        
        int totalHeight = getHeight();
        float textHeight = getPaint().measureText(getText().toString());
        int drawablePadding = getCompoundDrawablePadding();
        Drawable[] drawables = getCompoundDrawables();
        int drawableHeight = 0;
        if (drawables[0] != null){
            drawableHeight = getCompoundDrawables()[1].getIntrinsicHeight();
        }
        float paddingTop = (totalHeight - (textHeight + drawablePadding + drawableHeight)) / 2.0f;
        setPadding(0, (int)paddingTop, 0, 0);
        super.onDraw(canvas);
    }
}
