package com.poshwash.android.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.poshwash.android.R;
import com.valdesekamdem.library.mdtoast.MDToast;

public class BaseActivity extends AppCompatActivity {
    public Context mContext;
    private Activity mActivity;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;
        mActivity = this;
    }

    public static void showErrorSnackBar(View view, String msg, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_400));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(Context mContext) {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void sucessToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(getBaseContext(), message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public void errorToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(getBaseContext(), message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public void warningToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(getBaseContext(), message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }
}
