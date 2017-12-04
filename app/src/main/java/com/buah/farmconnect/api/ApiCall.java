package com.buah.farmconnect.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiCall {

    @GET("login.php")
    Call<Result> userLogin(@Query("email") String email,
                           @Query("password") String password) ;

    @GET("getallproducts.php")
    Call<Result> products(@Query("key") String key) ;

    @GET("getproducts.php")
    Call<Result> mproducts(@Query("userid") String userid);

    @GET("getcategory.php")
    Call<Result> category();

    @GET("getprofile.php")
    Call<Result> userprofile(@Query("userid") String userid);

    @GET("getproductdetail.php")
    Call<Result> productdetails(@Query("productid") String productid);


    @FormUrlEncoded
    @POST("userupdate.php")
    Call<Result> userUpdate(@Field("userid") String id,
                            @Field("fullname") String fullname,
                            @Field("email") String email,
                            @Field("contact") String contact);

    @FormUrlEncoded
    @POST("usersignup.php")
    Call<Result> userSignup(@Field("fullname") String fullname,
                            @Field("email") String email,
                            @Field("password") String password,
                            @Field("contact") String contact);


}


