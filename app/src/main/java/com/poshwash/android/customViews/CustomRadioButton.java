package com.poshwash.android.customViews;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by anandj on 3/20/2017.
 */
@SuppressLint("AppCompatCustomView")
public class CustomRadioButton extends RadioButton {


    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/jf_flat_regular.ttf");
        setTypeface(tf);

    }
}
