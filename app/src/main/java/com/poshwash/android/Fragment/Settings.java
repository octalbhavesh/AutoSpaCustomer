package com.poshwash.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Activity.StaticPage;
import com.poshwash.android.Adapter.SettingAdapter;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.RecyclerItemClickListener;
import com.poshwash.android.Utils.Util;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by anandj on 3/17/2017.
 */

public class Settings extends Fragment {
    static Context context;
    private ImageView toggle_icon;
    private TextView txt_heading;
    private RelativeLayout rr_toolbar;
    private RecyclerView recyclerview;
    private ArrayList<String> setting_name;
    private SettingAdapter sAdapter;
    MyProgressDialog myProgressDialog;

    public static Settings newInstance(Context contex) {
        Settings f = new Settings();
        context = contex;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        context = getActivity();
        setting_name = new ArrayList<>();
        myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.setCancelable(false);
        try {
            ZopimChat.init("IoFLpzRqCwUGKDyclVuGlsnIsSA71mUX");
            VisitorInfo visitorInfo = new VisitorInfo.Builder()
                    .name(MySharedPreferences.getFirstName(getActivity()))
                    .email(MySharedPreferences.getUserEmail(getActivity()))
                    .phoneNumber(MySharedPreferences.getPhoneNumber(getActivity()))
                    .build();
            ZopimChat.setVisitorInfo(visitorInfo);

        }
        catch (Exception e){

        }


        initView(view);
        ((MainActivity) getActivity()).setSettingFragment();

        return view;
    }

    private void initView(View view) {
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        txt_heading = (TextView) view.findViewById(R.id.txt_heading);
        rr_toolbar = (RelativeLayout) view.findViewById(R.id.rr_toolbar);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        sAdapter = new SettingAdapter(context, setting_name);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(sAdapter);

        recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = null;
                        switch (position) {
                            case 0:
                                Log.e("LoginType  : ", "LoginType : " + MySharedPreferences.getLoginType(context));
                                if (MySharedPreferences.getLoginStatus(context).equals("")) {
                                    Util.showAlertDialog(context, getString(R.string.app_name), getString(R.string.change_lang_alert));
                                } else {
                                    selectLanguage(context);
                                }
                                // startActivity(new Intent(getActivity(), ChangePassword.class));
                                break;
                            case 1:
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "privacy_policy");
                                intent.putExtra("pagename", getString(R.string.privacy_policy));
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "terms_of_use");
                                intent.putExtra("pagename", getString(R.string.terms_and_condition));
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "faq");
                                intent.putExtra("pagename", getString(R.string.faq));
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "about_us");
                                intent.putExtra("pagename", getString(R.string.About_us));
                                startActivity(intent);
                                break;
                            case 5:
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "how_its_work");
                                intent.putExtra("pagename", getString(R.string.help));
                                startActivity(intent);
                                break;
                            case 6:
                                startActivity(new Intent(getActivity(), ZopimChatActivity.class));
                                break;
                            case 7:
                             /*   Intent intent3 = new Intent(Intent.ACTION_SEND);
                                intent3.setType("plain/text");
                                intent3.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@autospa.com"});
                                intent3.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contactus));
                                intent3.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(Intent.createChooser(intent3, getString(R.string.send_email)));*/
                                intent = new Intent(getActivity(), StaticPage.class);
                                intent.putExtra("pagetype", "contact_us");
                                intent.putExtra("pagename", getString(R.string.contactus));
                                startActivity(intent);
                                break;

                        }
                    }
                })
        );

        fillData();
    }

    private void fillData() {

        setting_name.add(getString(R.string.change_language));
        setting_name.add(getString(R.string.privacyplicies));
        setting_name.add(getString(R.string.termscondition));
        setting_name.add(getString(R.string.faq));
        setting_name.add(getString(R.string.aboutus));
        setting_name.add(getString(R.string.help));
        setting_name.add(getString(R.string.liveChatandTickets));
        //   setting_name.add(getString(R.string.liveChat));
        setting_name.add(getString(R.string.contactus));

        sAdapter.notifyDataSetChanged();
    }

    public void selectLanguage(final Context context) {
        final String[] items = {context.getString(R.string.english), context.getString(R.string.arabic)};
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.select_language_dialog);

        final RadioButton c1 = (RadioButton) dialog1.findViewById(R.id.radio1);
        final RadioButton c2 = (RadioButton) dialog1.findViewById(R.id.radio2);
        final TextView doneTv = (TextView) dialog1.findViewById(R.id.doneTv);
        final RadioGroup rbgroup = (RadioGroup) dialog1.findViewById(R.id.rbgroup);

        if (MySharedPreferences.getLanguage(context).compareToIgnoreCase("eng") == 0) {
            c1.setChecked(true);
            c2.setChecked(false);
        } else {
            c1.setChecked(false);
            c2.setChecked(true);
        }

        doneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lang = "";
                if (rbgroup.getCheckedRadioButtonId() == R.id.radio1) {
                    lang = "eng";
                } else {
                    lang = "ara";
                }
                // dialog1.dismiss();
                MySharedPreferences.setLanguage(context, lang);
                dialog1.dismiss();
                AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                getActivity().finish();
                //   callWebServiceChangeLanguage(lang, dialog1);


            }
        });

        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog1.show();
    }

    public void callWebServiceChangeLanguage(final String language, final Dialog dialog1) {
        myProgressDialog.show();
        Call<ResponseBody> call;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject.put("language", language);
            jsonObject.put("langs", language);
            jsonObject.put("user_type", "1");

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.updateLanguage(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Util.dismissPrograssDialog(myProgressDialog);
                    try {
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            MySharedPreferences.setLanguage(context, language);
                            dialog1.dismiss();
                            AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
                            Intent intent = getActivity().getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            getActivity().finish();
                        }
                    } catch (Exception e) {
                        Log.e("", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(myProgressDialog);
                }
            });

        } catch (Exception e) {
            Util.dismissPrograssDialog(myProgressDialog);
            Log.e("", e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }
}
