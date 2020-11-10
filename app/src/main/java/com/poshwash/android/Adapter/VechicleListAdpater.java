package com.poshwash.android.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Beans.VechicleListModal;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Fragment.VechicleList;
import com.poshwash.android.R;

import java.util.List;

public class VechicleListAdpater extends RecyclerView.Adapter<VechicleListAdpater.MyViewHolder> {

    private List<VechicleListModal> vechicleLists;
    private VechicleList fragment;
    private Context context;
    DisplayImageOptions options;

    public VechicleListAdpater(VechicleList fragment, List<VechicleListModal> vechicleList, Context context) {
        this.vechicleLists = vechicleList;
        this.fragment = fragment;
        this.context = context;
        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehiclelist_child, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VechicleListModal vechicleListModal = vechicleLists.get(position);
        if (!vechicleListModal.getPhoto().equals(""))
            ImageLoader
                    .getInstance()
                    .displayImage(GlobalControl.IMAGE_BASE_URL + vechicleListModal.getPhoto(),
                    holder.img_car, options);

        holder.txt_carname.setText(" " + vechicleListModal.getMake());
        holder.txt_modalnumber.setText(" " + vechicleListModal.getModel());
        holder.txt_plate_number.setText(" " + vechicleListModal.getPlate_number());
        holder.txt_colornmame.setText(" " + vechicleListModal.getColor());
        holder.deleteTv.setTag(position);

        holder.deleteTv.setTag(position);
    }

    @Override
    public int getItemCount() {
        return vechicleLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_car;
        TextView txt_carname, txt_modalnumber, txt_plate_number, txt_colornmame, editTv, deleteTv;
        SwipeRevealLayout swipeRefresh;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_car = (ImageView) itemView.findViewById(R.id.img_car);
            txt_carname = (TextView) itemView.findViewById(R.id.txt_carname);
            txt_modalnumber = (TextView) itemView.findViewById(R.id.txt_modalnumber);
            txt_plate_number = (TextView) itemView.findViewById(R.id.txt_plate_number);
            txt_colornmame = (TextView) itemView.findViewById(R.id.txt_colornmame);
            editTv = (TextView) itemView.findViewById(R.id.editTv);
            deleteTv = (TextView) itemView.findViewById(R.id.deleteTv);
            swipeRefresh = (SwipeRevealLayout) itemView.findViewById(R.id.swipeRefresh);

            editTv.setOnClickListener(this);
            deleteTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.editTv:
                    fragment.editVehicleItem(getAdapterPosition());
                    swipeRefresh.close(true);
                    break;
                case R.id.deleteTv:
                    //Dynamic Data
//                    int position = (int) view.getTag();
                    swipeRefresh.close(true);
                    //Dynamic Data
                  /*  if (vechicleLists.get(position).getDelete_status() == 0) {
                        Util.showAlertDialog(context, context.getString(R.string.app_name), context.getString(R.string.notdelete_vehicle));
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.are_you_sure_you_want_to_delete));
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                fragment.removeVehcleItem(getAdapterPosition());
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog d = builder.show();
                        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                        int btn = d.getContext().getResources().getIdentifier("android:id/button1", null, null);
                        TextView tv = (TextView) d.findViewById(textViewId);
                        Button nbutton1 = (Button) d.findViewById(btn);
                        tv.setTextColor(ContextCompat.getColor(context, R.color.skybluecolor));
                        nbutton1.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
                        int btn1 = d.getContext().getResources().getIdentifier("android:id/button2", null, null);
                        Button nbutton2 = (Button) d.findViewById(btn1);
                        nbutton2.setTextColor(ContextCompat.getColor(context, R.color.graycolor));
                    }*/

                    //Static Data
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.are_you_sure_you_want_to_delete));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Dialog d = builder.show();
                    int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    int btn = d.getContext().getResources().getIdentifier("android:id/button1", null, null);
                    TextView tv = (TextView) d.findViewById(textViewId);
                    Button nbutton1 = (Button) d.findViewById(btn);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.skybluecolor));
                    nbutton1.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
                    int btn1 = d.getContext().getResources().getIdentifier("android:id/button2", null, null);
                    Button nbutton2 = (Button) d.findViewById(btn1);
                    nbutton2.setTextColor(ContextCompat.getColor(context, R.color.graycolor));
                    break;
            }
        }
    }
}