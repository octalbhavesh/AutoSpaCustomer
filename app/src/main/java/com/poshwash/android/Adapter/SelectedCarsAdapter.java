package com.poshwash.android.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Beans.BeanCars;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.R;
import com.poshwash.android.customViews.CustomTexViewRegular;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anand jain on 3/20/2017.
 */

public class SelectedCarsAdapter extends RecyclerView.Adapter<SelectedCarsAdapter.MyViewHolder> {

    private List<BeanCars> carsList;
    DisplayImageOptions options;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView vehicleIv;
        CustomTexViewRegular vehicleNameTv;
        CustomTexViewRegular vehicleCountTv;

        public MyViewHolder(View view) {
            super(view);

            vehicleIv = (CircleImageView) itemView.findViewById(R.id.vehicleIv);
            vehicleNameTv = (CustomTexViewRegular) itemView.findViewById(R.id.vehicleNameTv);
            vehicleCountTv = (CustomTexViewRegular) itemView.findViewById(R.id.vehicleCountTv);
        }
    }

    public SelectedCarsAdapter(Context context, List<BeanCars> carsList) {
        this.carsList = carsList;
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
                .inflate(R.layout.selected_cars_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.vehicleNameTv.setText(carsList.get(position).getCar_name());
        // holder.vehicleCountTv.setText(carsList.get(position).getCarCount() + "");
        if (!carsList.get(position).getCar_image().equals(""))
            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + carsList.get(position).getCar_image(), holder.vehicleIv, options);
        else
            holder.vehicleIv.setImageResource(R.drawable.aboutusbanner);


    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }

}
