package com.example.tutor.finder1.Helper;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquereImageView extends AppCompatImageView {
    public SquereImageView(Context context) {
        super(context);
    }

    public SquereImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquereImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
