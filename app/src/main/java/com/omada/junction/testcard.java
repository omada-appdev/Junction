package com.omada.junction;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

public class testcard extends MaterialCardView {
    public testcard(Context context) {
        super(context);
    }

    public testcard(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.materialCardViewStyle);
    }

    public testcard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
