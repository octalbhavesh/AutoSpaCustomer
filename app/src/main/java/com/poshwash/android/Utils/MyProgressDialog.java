package com.poshwash.android.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.poshwash.android.R;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MyProgressDialog extends AlertDialog {
    ImageView rotateImage;

    public MyProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {

        super.show();
        setContentView(R.layout.customprogressbar);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startRotatingImage();

    }

    @Override
    public void onBackPressed() {
        cancel();
        super.onBackPressed();
    }


    public void startRotatingImage() {
        rotateImage = (ImageView) findViewById(R.id.rotate_image);
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        rotateImage.startAnimation(startRotateAnimation);
    }

}