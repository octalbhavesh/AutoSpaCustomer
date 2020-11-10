package com.poshwash.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.poshwash.android.Activity.AddVehicle;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Adapter.VechicleListAdpater;
import com.poshwash.android.Beans.VechicleListModal;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.SwipeHelper;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VechicleList extends Fragment implements View.OnClickListener {
    static Context context;
    private TextView no_data_tv;
    private RecyclerView recylerview;
    private List<VechicleListModal> vechicleListModalList;
    private VechicleListAdpater adpater;
    private ImageView add_vechicle;
    private MyProgressDialog myProgressDialog;
    private final String TAG = "VechicleList.java";
    private Paint p = new Paint();
    private ButtonsState buttonShowedState = ButtonsState.GONE;

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    public static VechicleList newInstance(Context contex) {
        VechicleList f = new VechicleList();
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
        View view = inflater.inflate(R.layout.vehiclelist, container, false);
        context = getActivity();
        Env.currentActivity = getActivity();
        initView(view);
        ((MainActivity) getActivity()).setManageVechileFragment();
        return view;
    }

    private void initView(View view) {

        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        recylerview = (RecyclerView) view.findViewById(R.id.recylerview);
        add_vechicle = (ImageView) view.findViewById(R.id.add_vechicle);
        vechicleListModalList = new ArrayList<>();
        adpater = new VechicleListAdpater(this, vechicleListModalList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recylerview.setAdapter(adpater);
        add_vechicle.setOnClickListener(this);

        setSwipeToShowDeleteButton();
    }

    private void setSwipeToShowDeleteButton() {

        new SwipeHelper(Env.currentActivity, recylerview) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton("Delete", 0, Color.parseColor("#dc1100"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                String vehicleId = vechicleListModalList.get(position).getId();
                                dialogDelete(vehicleId);
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton("Edit", 0, Color.parseColor("#006400"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                String notificationID = vechicleListModalList.get(position).getId();
                                editVehicleItem(position);
                            }
                        }
                ));
            }
        };
    }

    private void dialogDelete(final String vehicleId) {
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_vehicle_delete);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();

        TextView dialog_yes = (TextView) dialog.findViewById(R.id.dialog_yes);
        TextView dialog_no = (TextView) dialog.findViewById(R.id.dialog_no);

        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeVehcleItem(vehicleId);
                dialog.dismiss();
            }
        });

        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void replaceFragment() {
        Intent intent = new Intent(getActivity(), AddVehicle.class);
        intent.putExtra("type", "add");
     //   startActivityForResult(intent, 1);
         startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_vechicle:
                replaceFragment();
                break;
        }
    }

    private void callWebServiceShowVehicleList() {
        Util.showProgress(getActivity());
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject1.put("language", MySharedPreferences.getLanguage(getActivity()));
            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.vehicleList(new ConvertJsonToMap().jsonToMap(jsonObject1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.hideProgress();
                    try {
                        JSONObject jsonObject11 = new JSONObject(response.body().string());
                        if (jsonObject11.getJSONObject("response").getBoolean("status")) {
                            JSONArray jsonArray = jsonObject11.getJSONObject("response").getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                no_data_tv.setVisibility(View.GONE);
                                recylerview.setVisibility(View.VISIBLE);
                                receiveVehicleList(jsonArray);
                            } else {
                                no_data_tv.setVisibility(View.VISIBLE);
                                recylerview.setVisibility(View.GONE);
                            }
                        } else {
                            no_data_tv.setVisibility(View.VISIBLE);
                            recylerview.setVisibility(View.GONE);
                            //  Util.showAlertDialogWithNoTitle(getActivity(), jsonObject11.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(myProgressDialog);
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(myProgressDialog);
            Log.e(TAG, e.toString());
        }
    }

    public void editVehicleItem(int pos) {

        /* Intent intent = new Intent(getActivity(), AddVehicle.class);
        Bundle bundle = new Bundle();
        bundle.putString("vehicle_id", vechicleListModalList.get(pos).getId());
        bundle.putString("Vehicle_type", vechicleListModalList.get(pos).getVehicle_type());
        bundle.putString("Vehicle_type_id", vechicleListModalList.get(pos).getVehicle_type_id());
        bundle.putString("model", vechicleListModalList.get(pos).getModel());
        bundle.putString("plate_number", vechicleListModalList.get(pos).getPlate_number());
        bundle.putString("color", vechicleListModalList.get(pos).getColor());
        bundle.putString("photo", vechicleListModalList.get(pos).getPhoto());
        intent.putExtra("vehicledata", bundle);
        startActivityForResult(intent, 1);*/


        Intent i = new Intent(getActivity(), AddVehicle.class);
        i.putExtra("plate_number", vechicleListModalList.get(pos).getPlate_number());
        i.putExtra("type", "edit");
        i.putExtra("color", vechicleListModalList.get(pos).getColor());
        i.putExtra("id", vechicleListModalList.get(pos).getId());
        i.putExtra("vehicle_id", vechicleListModalList.get(pos).getVehicle_type_id());
        startActivity(i);
    }

    public void removeVehcleItem(String pos) {
        Util.showProgress(getActivity());
        Call<ResponseBody> call = null;
        JSONObject jsonObject = null;
        String ss=MySharedPreferences.getLanguage(getActivity());
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject.put("vehicle_id", pos);
            jsonObject.put("language", "eng");

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.deleteVehicle(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.hideProgress();
                    try {
                        JSONObject jsonObject1 = new JSONObject(Util.convertRetrofitResponce(response));
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            Util.Toast(getActivity(), jsonObject1.getJSONObject("response").getString("message"));
                            callWebServiceShowVehicleList();
                        } else {
                            Util.Toast(getActivity(), jsonObject1.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.hideProgress();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Util.hideProgress();
        }
    }

    public void receiveVehicleList(JSONArray jsonArray) {
        try {
            vechicleListModalList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                VechicleListModal vechicleListModal = new VechicleListModal();
                JSONObject jsonObject1 = jsonObject.optJSONObject("VehicleType");
                vechicleListModal.setName(jsonObject1.optString("name"));

                JSONObject jsonMake = jsonObject.optJSONObject("Make");
                vechicleListModal.setMake(jsonMake.optString("name"));

                JSONObject jsonModel = jsonObject.optJSONObject("VehicleModel");
                vechicleListModal.setModel(jsonModel.optString("name"));

                JSONObject jsonInfo = jsonObject.optJSONObject("UserVehicle");
                vechicleListModal.setId(jsonInfo.getString("id"));
                vechicleListModal.setMake_id(jsonInfo.getString("make_id"));
                vechicleListModal.setPlate_number(jsonInfo.getString("plate_number"));
                vechicleListModal.setColor(jsonInfo.getString("color"));
                vechicleListModal.setPhoto(jsonInfo.getString("vehicle_picture"));
                vechicleListModal.setVehicle_type_id(jsonInfo.optString("vehicle_type_id"));//Vehicle_type
                vechicleListModalList.add(vechicleListModal);
            }
            adpater.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity = getActivity();
        if(Util.isConnectingToInternet(getActivity()))
        callWebServiceShowVehicleList();
        else
            Util.errorToast(getActivity(),getActivity().getString(R.string.no_internet_connection));

        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
//        callWebServiceShowVehicleList();
    }
}
