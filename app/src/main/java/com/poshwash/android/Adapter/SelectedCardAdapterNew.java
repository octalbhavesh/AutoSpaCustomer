package com.poshwash.android.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Beans.BeanCarsNew;
import com.poshwash.android.Fragment.HomeFragment;
import com.poshwash.android.Fragment.Notification;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;

import java.util.ArrayList;

/**
 * Created by anand jain on 3/20/2017.
 */

public class SelectedCardAdapterNew extends RecyclerView.Adapter<SelectedCardAdapterNew.MyViewHolder> {

    private ArrayList<BeanCarsNew> moviesList;
    DisplayImageOptions options;
    Context context;
    Notification notification;
    MyProgressDialog myProgressDialog;
    private int radioBTSelectedPos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;
        private RadioButton rbCarName;

        public MyViewHolder(View view) {
            super(view);
            rbCarName = (RadioButton) view.findViewById(R.id.check_img);
            txt_name = (TextView) view.findViewById(R.id.car_name);
            //     frameDelete.setOnClickListener(this);
        }


    }

    public SelectedCardAdapterNew(Context context, ArrayList<BeanCarsNew> moviesList) {
        this.moviesList = moviesList;
        this.context = context;

        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defalut_bg);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_car_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.rbCarName.setText(moviesList.get(position).getMake() + " - " + moviesList.get(position).getModel()
        +" - " + moviesList.get(position).getPlate_number());

     /*   if (radioBTSelectedPos != -1 && radioBTSelectedPos == position) {
            moviesList.get(position).setClick(true);
        } else {
            moviesList.get(position).setClick(false);
        }*/


        holder.rbCarName.setChecked(position == radioBTSelectedPos);
        holder.rbCarName.setTag(position);

        holder.rbCarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               radioBTSelectedPos = (int) view.getTag();
                HomeFragment.mSelectedVId = moviesList.get(position).getV_id();
                HomeFragment.mSelectedVNAME = moviesList.get(position).getV_name();
                HomeFragment.mSelectedVehicleTypeId = moviesList.get(position).getVehicle_type_id();
                HomeFragment.mSelectedCarId = moviesList.get(position).getId();
                HomeFragment.mSelectedCarName = moviesList.get(position).getName();
                HomeFragment.mSelectedCarPlate = moviesList.get(position).getPlate_number();
                notifyDataSetChanged();
            }
        });



       /* holder.rbCarName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && compoundButton.isPressed()) {


                    radioBTSelectedPos = position;




                    HomeFragment.mSelectedCar = moviesList.get(position).getVehicle_type_id();
                    HomeFragment.mSelectedCarId = moviesList.get(position).getId();
                    HomeFragment.mSelectedCarName = moviesList.get(position).getName();
                    HomeFragment.mSelectedCarPlate = moviesList.get(position).getPlate_number();
                    notifyDataSetChanged();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size(); //Dynamic Data moviesList.size();
    }


}
