package com.buah.farmconnect.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Multipart
    @POST("uploadproduct.php")
    Call<Result> uploadMulFile( @Part("userid") RequestBody id,
                                @Part("categoryid") RequestBody categoryid,
                                @Part("productname") RequestBody productname,
                                @Part("price") RequestBody price,
                                @Part("description") RequestBody description,
                                @Part("location") RequestBody location,
                                @Part MultipartBody.Part file1,
                                @Part MultipartBody.Part file2,
                                @Part MultipartBody.Part file3,
                                @Part MultipartBody.Part file4,
                                @Part MultipartBody.Part file5,
                                @Part MultipartBody.Part file6
    );
}


