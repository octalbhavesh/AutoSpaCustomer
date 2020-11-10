package com.poshwash.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Activity.BookingDetail;
import com.poshwash.android.Beans.MyBookingModal;
import com.poshwash.android.R;

import java.util.List;

public class MyBookingAdapter extends BaseExpandableListAdapter {
    Context context;
    List<MyBookingModal> myBookingModals;
    ExpandableListView expandableListView;
    DisplayImageOptions options;
    boolean isVisibleCancelButton;

    //Constructor to initialize values
    public MyBookingAdapter(Context context, List<MyBookingModal> myBookingModals, ExpandableListView expandableListView, boolean isVisibleCancelButton) {
        this.isVisibleCancelButton = isVisibleCancelButton;
        this.context = context;
        this.myBookingModals = myBookingModals;
        this.expandableListView = expandableListView;
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
    public int getGroupCount() {
        return 10;// Dynamic data myBookingModals.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;//Dynamic data myBookingModals.get(i).getMyBookingChildModals().size();
    }

    @Override
    public Object getGroup(int i) {
        return myBookingModals.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;//Dynamic data myBookingModals.get(i).getMyBookingChildModals().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.mybooking_parent, null);
        }
        view.setClickable(false);
        view.setFocusable(false);
        expandableListView.expandGroup(i);
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.mybooking_child, null);
        }
        //Dynamic Data
//        final MyBookingChildModal myBookingChildModal = myBookingModals.get(i).getMyBookingChildModals().get(i1);
        RelativeLayout rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        final ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
//        if (isVisibleCancelButton && myBookingChildModal.getStatus().compareTo("1") == 0) {
//            iv_cancel.setVisibility(View.VISIBLE);
//        } else {
        iv_cancel.setVisibility(View.GONE);
//        }
//        ImageView img_car = (ImageView) view.findViewById(R.id.img_car);
//        if (myBookingChildModal.getImage() != null && !myBookingChildModal.getImage().equals("")) {
//            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + myBookingChildModal.getImage(), img_car, options);
//        } else {
//            img_car.setImageResource(R.drawable.aboutusbanner);
//        }

//        TextView txt_carname = (TextView) view.findViewById(R.id.txt_carname);
//        if (myBookingChildModal.getCar_name() != null)
//            txt_carname.setText(myBookingChildModal.getCar_name());
//
//        TextView txt_username = (TextView) view.findViewById(R.id.txt_username);
//        txt_username.setText(myBookingChildModal.getOwner_name());
//
//        RatingBar rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
//        rating_bar.setRating(Float.parseFloat(myBookingChildModal.getRating_count()));
//
//        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
//        txt_price.setText("" + Util.DisplayAmount(myBookingChildModal.getPrice()));
//
//        TextView txt_time = (TextView) view.findViewById(R.id.txt_time);
        TextView viewDeatilTv = (TextView) view.findViewById(R.id.viewDeatilTv);
//        try {
//            txt_time.setText(Util.convertUtcTimeToLocal(myBookingChildModal.getBooking_date() + " " + myBookingChildModal.getBooking_time(), true, context));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        TextView status_tv = (TextView) view.findViewById(R.id.status_tv);
//        View view1 = view.findViewById(R.id.divider);
//        if (i1 == myBookingModals.get(i).getMyBookingChildModals().size() - 1)
//            view1.setVisibility(View.GONE);
//        else
//            view1.setVisibility(View.VISIBLE);
//        status_tv.setVisibility(View.VISIBLE);
//        if (myBookingChildModal.getStatus().compareTo("0") == 0) {
//            status_tv.setText(context.getString(R.string.initiated));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.pending_color));
//        } else if (myBookingChildModal.getStatus().compareTo("1") == 0) {
//            status_tv.setText(context.getString(R.string.accepted));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.accepted_color));
//        } else if (myBookingChildModal.getStatus().compareTo("2") == 0) {
//            status_tv.setText(context.getString(R.string.completed));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.complete_color));
//        } else if (myBookingChildModal.getStatus().compareTo("3") == 0) {
//            status_tv.setText(context.getString(R.string.cancelled));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.cancel_color));
//        } else if (myBookingChildModal.getStatus().compareTo("4") == 0) {
//            status_tv.setText(context.getString(R.string.refunded));
//        } else if (myBookingChildModal.getStatus().compareTo("5") == 0) {
//            status_tv.setText(context.getString(R.string.startwash));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.started_color));
//        } else if (myBookingChildModal.getStatus().compareTo("7") == 0) {
//            status_tv.setText(context.getString(R.string.cancel_by_admin));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.cancel_color));
//        } else if (myBookingChildModal.getStatus().compareTo("8") == 0) {
//            status_tv.setText(context.getString(R.string.cancel_by_customer));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.cancel_color));
//        } else if (myBookingChildModal.getStatus().compareTo("9") == 0) {
//            status_tv.setText(context.getString(R.string.reached));
//            status_tv.setTextColor(ContextCompat.getColor(context, R.color.reached_color));
//        }

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (myBookingChildModal.getStatus().compareTo("1") == 0) {
                    iv_cancel.setVisibility(View.VISIBLE);*/
//                Intent intent = new Intent(context, CancelBooking.class);
//                intent.putExtra("booking_id", myBookingModals.get(i).getMyBookingChildModals().get(i1).getId());
//                intent.putExtra("booking_driver_id", myBookingModals.get(i).getMyBookingChildModals().get(i1).getBooking_Sp_id());
//                ((Activity) context).startActivityForResult(intent, 102);
//                ((Activity) context).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                /*} else {
                    iv_cancel.setVisibility(View.GONE);*/
//                }
            }
        });

        viewDeatilTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dynamic Data
//                Intent intent = new Intent(context, BookingDetail.class);
//                intent.putExtra("bookingId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getId());
//                intent.putExtra("bookingDriverId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getBooking_Sp_id());
//                context.startActivity(intent);
                //Static Data
                //Static Data
                Intent intent = new Intent(context, BookingDetail.class);
                intent.putExtra("bookingId", "");
                intent.putExtra("bookingDriverId", "");
                context.startActivity(intent);
            }
        });

        rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (myBookingChildModal.getStatus().compareTo("1") == 0 ||
//                        myBookingChildModal.getStatus().compareTo("5") == 0 ||
//                        myBookingChildModal.getStatus().compareTo("9") == 0) {
//                    String bookingtime = "";
//                    try {
//                        bookingtime = Util.convertUtcTimeToLocal(myBookingChildModal.getBooking_date() + " " + myBookingChildModal.getBooking_time(), true, context);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    if (Util.getTimeDifference(bookingtime)) {
//                        ((MainActivity) context).displayView(AutoSpaConstant.HOMEFRAGMENT, myBookingChildModal, null);
//                    } else {
//                        Intent intent = new Intent(context, BookingDetail.class);
//                        intent.putExtra("bookingId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getId());
//                        intent.putExtra("bookingDriverId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getBooking_Sp_id());
//                        context.startActivity(intent);
//                    }
//                } else {
//                    Intent intent = new Intent(context, BookingDetail.class);
//                    intent.putExtra("bookingId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getId());
//                    intent.putExtra("bookingDriverId", myBookingModals.get(i).getMyBookingChildModals().get(i1).getBooking_Sp_id());
//                    context.startActivity(intent);
//                }

                //Static Data
                Intent intent = new Intent(context, BookingDetail.class);
                intent.putExtra("bookingId", "");
                intent.putExtra("bookingDriverId", "");
                context.startActivity(intent);
            }
        });

//        if (myBookingChildModal.getStatus().compareTo("1") == 0 || myBookingChildModal.getStatus().compareTo("5") == 0 || myBookingChildModal.getStatus().compareTo("9") == 0) {
//            viewDeatilTv.setVisibility(View.VISIBLE);
//        } else {
//            viewDeatilTv.setVisibility(View.GONE);
//        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
