package com.poshwash.android.Delegate;


import com.poshwash.android.Beans.PackagesData;
import com.poshwash.android.response.LoginResponse;
import com.poshwash.android.response.OtpConfirmResponse;
import com.poshwash.android.response.RegisterResponse;
import com.poshwash.android.response.StaticPageResponse;
import com.poshwash.android.response.TransactionResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/*
 * Created by monikab on 8/3/2016.
 */
public interface MyApiEndpointInterface {

    @POST("signup")
    Call<RegisterResponse> register(@Body Map<String, Object> changePasswordRequest);

    @POST("otp_confirmation")
    Call<OtpConfirmResponse> otp_confirmation(@Body Map<String, Object> changePasswordRequest);

    @POST("resend_otp")
    Call<ResponseBody> resend_otp(@Body Map<String, Object> changePasswordRequest);

    @POST("login")
    Call<LoginResponse> login(@Body Map<String, Object> changePasswordRequest);

    @POST("save_card_details")
    Call<ResponseBody> save_card(@Body Map<String, Object> changePasswordRequest);

    @POST("pages_view")
    Call<StaticPageResponse> pages_view(@Body Map<String, Object> changePasswordRequest);

    @POST("update_notification_setting")
    Call<LoginResponse> update_notification_setting(@Body Map<String, Object> changePasswordRequest);

    @POST("notification_list")
    Call<ResponseBody> notification_list(@Body Map<String, Object> changePasswordRequest);

    @POST("driver_rating")
    Call<ResponseBody> driver_rating(@Body Map<String, Object> changePasswordRequest);

    @POST("get_nearest_sp")
    Call<ResponseBody> get_nearest_sp(@Body Map<String, Object> changePasswordRequest);

    @POST("get_card_details")
    Call<ResponseBody> get_card_details(@Body Map<String, Object> changePasswordRequest);

    @POST("cancel_booking_by_customer")
    Call<ResponseBody> cancel_booking_by_customer(@Body Map<String, Object> changePasswordRequest);

    @POST("booking_detail")
    Call<ResponseBody> booking_detail(@Body Map<String, Object> changePasswordRequest);


    @POST("booking_saves")
    Call<ResponseBody> booking_saves(@Body Map<String, Object> changePasswordRequest);

    @POST("apply_refer_code")
    Call<ResponseBody> apply_refer_code(@Body Map<String, Object> changePasswordRequest);

    @Multipart
    @POST("add_vehicle")
    Call<ResponseBody> add_vehicle(@PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("add_vehicle")
    Call<ResponseBody> register_two(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part imageFile);

    @POST("past_bookings")
    Call<ResponseBody> past_bookings(@Body Map<String, Object> changePasswordRequest);

    @POST("my_bookings")
    Call<ResponseBody> my_bookings(@Body Map<String, Object> changePasswordRequest);

    @POST("transaction_list")
    Call<TransactionResponse> transaction_list(@Body Map<String, Object> changePasswordRequest);

    @POST("generate_braintree_token")
    Call<ResponseBody> generate_braintree_token(@Body Map<String, Object> changePasswordRequest);

    @POST("add_wallet_amount")
    Call<ResponseBody> add_wallet_amount(@Body Map<String, Object> changePasswordRequest);

    /*----------------------------------------------------------------------------------------*/

    @POST("change_password")
    Call<ResponseBody> changePassword(@Body Map<String, Object> changePasswordRequest);


    @POST("social_login")
    Call<ResponseBody> socailLogin(@Body Map<String, Object> socailloginRequest);

    @POST("signup")
    Call<ResponseBody> normalUserRegister(@Body Map<String, Object> socailloginRequest);

    @POST("forget_password")
    Call<ResponseBody> forgotPassword(@Body Map<String, Object> forgotRequest);

    @POST("social_exist")
    Call<ResponseBody> socialExist(@Body Map<String, Object> socialexistRequest);

    @Multipart
    @POST("edit_profile")
    Call<ResponseBody> editProfile(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("edit_profile")
    Call<ResponseBody> editProfileWithoutImage(@PartMap Map<String, RequestBody> partMap);

    @POST("get_nearest_sp")
    Call<ResponseBody> finNearServiceProvider(@Body Map<String, Object> findNearLocation);

    @POST("get_nearest_sp_by_branch_for_slot_new")
    Call<ResponseBody> getNearestSpByBranch(@Body Map<String, Object> getNearestSpByBranch);

    @POST("apply_promo_code")
    Call<ResponseBody> applyPromoCode(@Body Map<String, Object> findNearLocation);

    @POST("updatelocation")
    Call<ResponseBody> updateLocation(@Body Map<String, Object> updateLocation);

    @POST("pages_view")
    Call<ResponseBody> static_content(@Body Map<String, Object> static_contents);

    @POST("get_vehicle_types")
    Call<ResponseBody> showVechicleType(@Body Map<String, Object> showVechicleType);

    @Multipart
    @POST("add_vehicle")
    Call<ResponseBody> addVehicle(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("add_vehicle")
    Call<ResponseBody> addVehicle(@PartMap() Map<String, RequestBody> partMap);

    @POST("get_vehicle")
    Call<ResponseBody> vehicleList(@Body Map<String, Object> vechicle_list);



    @POST("check_user_location")
    Call<ResponseBody> check_user_location(@Body Map<String, Object> vechicle_list);

    @POST("delete_vehicle")
    Call<ResponseBody> deleteVehicle(@Body Map<String, Object> delete_vechicle);


    @POST("delete_card_details")
    Call<ResponseBody> delete_card_details(@Body Map<String, Object> delete_vechicle);

    @Multipart
    @POST("edit_vehicle")
    Call<ResponseBody> updateVehicle(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("edit_vehicle")
    Call<ResponseBody> updateVehicle(@PartMap() Map<String, RequestBody> partMap);

    @POST("cancel_booking_by_customer")
    Call<ResponseBody> cancelBookingUser(@Body Map<String, Object> cancel_booking);

    @POST("my_bookings")
    Call<ResponseBody> myBooking(@Body Map<String, Object> mybooking);

    @POST("transaction_list")
    Call<ResponseBody> myTransaction(@Body Map<String, Object> mytransaction);

    @POST("upcoming_booking")
    Call<ResponseBody> myUpcomingBooking(@Body Map<String, Object> mytransaction);

    @POST("notification_list")
    Call<ResponseBody> showNotification(@Body Map<String, Object> shownotification);

    @POST("update_language")
    Call<ResponseBody> updateLanguage(@Body Map<String, Object> shownotification);

    @POST("booking_saves")
    Call<ResponseBody> bookingSaves(@Body Map<String, Object> add_creditcard);

    @POST("booking_saves_with_slot")
    Call<ResponseBody> bookingSavesWithSlot(@Body Map<String, Object> booking_saves_with_slot);

    @POST("my_profile")
    Call<ResponseBody> getProfile(@Body Map<String, Object> getProfile);

    @POST("get_site_setting")
    Call<ResponseBody> getSiteSetting(@Body Map<String, Object> getProfile);

    @POST("logout")
    Call<ResponseBody> logOut(@Body Map<String, Object> logOut);

    @POST("cancel_booking")
    Call<ResponseBody> autoCancelBooking(@Body Map<String, Object> findNearLocation);

    @POST("driver_rating")
    Call<ResponseBody> ratings(@Body Map<String, Object> ratings);

    @POST("update_notification_setting")
    Call<ResponseBody> notificationStatus(@Body Map<String, Object> notificationStatus);

    @POST("otp_confirmation")
    Call<ResponseBody> verifyOtp(@Body Map<String, Object> verifyOtp);

    @POST("update_mobile_verify")
    Call<ResponseBody> updateMobileVerify(@Body Map<String, Object> updateMobileVerify);

    @POST("resend_otp")
    Call<ResponseBody> resendOtp(@Body Map<String, Object> resendOtp);

    @POST("get_fair_data")
    Call<ResponseBody> getFairData(@Body Map<String, Object> get_fair_data);

    @POST("generate_otp")
    Call<ResponseBody> generateOtp(@Body Map<String, Object> generate_otp);

    @POST("update_mobile")
    Call<ResponseBody> updateContactNumber(@Body Map<String, Object> generate_otp);

    /*@POST("booking_detail")
    Call<ResponseBody> booking_detail(@Body Map<String, Object> booking_detail);*/

    @POST("notification_status")
    Call<ResponseBody> readNotification(@Body Map<String, Object> read_notification);

    @POST("remove_notification")
    Call<ResponseBody> removeNotification(@Body Map<String, Object> removeNotification);

    @POST("booking_remove")
    Call<ResponseBody> booking_remove(@Body Map<String, Object> booking_remove);

    @POST("get_package_details")
    Call<PackagesData> get_package_details(@Body Map<String, Object> changePasswordRequest);

    @POST("purchase_package")
    Call<ResponseBody> purchase_package(@Body Map<String, Object> changePasswordRequest);

    @POST("get_make_modal")
    Call<ResponseBody> get_make_modal(@Body Map<String, Object> changePasswordRequest);

}