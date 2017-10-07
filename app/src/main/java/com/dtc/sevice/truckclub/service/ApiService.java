package com.dtc.sevice.truckclub.service;

import com.dtc.sevice.truckclub.model.TblCarDetail;
import com.dtc.sevice.truckclub.model.TblCarGroup;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblPicture;
import com.dtc.sevice.truckclub.model.TblProvince;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by admin on 9/20/2017 AD.
 */

public class ApiService {
    public static final String FORUM_SERVER_URL = "http://wisasoft.com:8994/truck_club";
    public static final String url_facebook = "https://graph.facebook.com/";
    public static final String pic_facebook = "/picture?type=large";

    private ForumApi mForumApi;
    public ApiService() {


        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FORUM_SERVER_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mForumApi = restAdapter.create(ForumApi.class);
    }

    public ForumApi getApi() {

        return mForumApi;
    }

    public interface ForumApi {

        @FormUrlEncoded
        @POST("/member_register.php")
        public Observable<TblMember> createMember(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email,
                                                  @Field("user_name") String user_name, @Field("tel") String tel, @Field("birth_date") String birth_date, @Field("sex") String sex,
                                                  @Field("password") String password, @Field("member_type") int member_type, @Field("authority") String authority, @Field("device_id") String device_id,
                                                  @Field("face_book_id") String face_book_id, @Field("name_pic_path") String name_pic_path);

        @FormUrlEncoded
        @POST("/car_register.php")
        public Observable<TblCarDetail> createCar(@Field("member_id") int member_id, @Field("license_plate") String license_plate, @Field("car_brand") String car_brand,@Field("province") String province,
                                                  @Field("car_model") String car_model, @Field("group_id") int group_id,@Field("car_tow") int car_tow, @Field("car_wheels") int car_wheels,
                                                  @Field("car_tons") int car_tons, @Field("option_trailer") int option_trailer,@Field("sum_weight") int sum_weight);

        @FormUrlEncoded
        @POST("/create_picture.php")
        public Observable<TblPicture> createPictureCar(@Field("car_id") int car_id, @Field("name_path") String name_path);

        @FormUrlEncoded
        @POST("/create_member_and_car.php")
        public Observable<TblMember> createMemberAndCar(@Field("data") ArrayList<TblMember> data);
//        @FormUrlEncoded
//        @POST("/create_member_and_car.php")
//        public Observable<TblMember> createMemberAndCar(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email,
//                                                        @Field("user_name") String user_name, @Field("tel") String tel, @Field("birth_date") String birth_date, @Field("sex") String sex,
//                                                        @Field("password") String password, @Field("member_type") int member_type, @Field("authority") String authority, @Field("device_id") String device_id,
//                                                        @Field("face_book_id") String face_book_id, @Field("name_pic_path") String name_pic_path,@Field("car_detail") TblCarDetail car_detail);

        @FormUrlEncoded
        @POST("/user_login.php")
        public Observable<TblMember> getLogin(@Field("user_name") String user_name, @Field("password") String password,@Field("authority") String authority,@Field("member_type") int member_type, @Field("device_id") String device_id,@Field("face_book_id") String face_book_id,@Field("token_firebase") String token_firebase);

        @FormUrlEncoded
        @POST("/update_status_member.php")
        public Observable<TblMember> updateStatusMember(@Field("member_id") int member_id,@Field("status") String status,@Field("status_id") int status_id);

        @POST("/get_province.php")
        public Observable<List<TblProvince>> getProvince();

        @POST("/get_car_group.php")
        public Observable<List<TblCarGroup>> getCarGroup();

        @FormUrlEncoded
        @POST("/user_search_driver_in_scope.php")
        public Observable<List<TblMember>> getDriverInScope(@Field("member_id") int member_id, @Field("lat") float lat,@Field("lon") float lon,@Field("radius") double radius);

        @FormUrlEncoded
        @POST("/user_update_profile.php")
        public Observable<TblMember> updateUserProfile(@Field("member_id") int member_id,@Field("first_name") String first_name, @Field("last_name") String last_name
                                                                , @Field("email") String email,@Field("tel") String tel,@Field("birth_date") String birth_date
                                                                ,@Field("sex") String sex,@Field("name_pic_path") String name_pic_path);

        @FormUrlEncoded
        @POST("/clear_member.php")
        public Observable<TblMember> logOut(@Field("member_id") int member_id,@Field("authority") String status);
//        @POST("/category.php")
//        public Observable<List<TblCategory>> getCategory();
//
//        @POST("/discount.php")
//        public Observable<List<Tbldiscount>> getDiscount();
//
//        @FormUrlEncoded
//        @POST("/myItem.php")
//        public Observable<List<TblMyItem>> getMyItem(@Field("o_vendor_id") int o_vendor_id);
//
//        @FormUrlEncoded
//        @POST("/getItemByType.php")
//        public Observable<List<TblMyItem>> getItemByType(@Field("o_c_id") int o_c_id);
//

//        @FormUrlEncoded
//        @POST("/loginAuthen.php")
//        public Observable<TblMember> getLogin(@Field("email") String email, @Field("password") String password);
//
//        @FormUrlEncoded
//        @POST("/updateProfile.php")
//        public Observable<TblMember> updateProfile(@Field("id") String id,@Field("first_name") String first_name, @Field("last_name") String last_name,@Field("email") String email, @Field("tel") String tel);
    }
}
