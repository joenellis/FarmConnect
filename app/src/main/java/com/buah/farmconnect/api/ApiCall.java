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

    @GET("verifyemail.php")
    Call<Result> verifyemail(@Query("email") String email);

    @GET("getallproducts.php")
    Call<Result> products(@Query("key") String key) ;

    @GET("getproducts.php")
    Call<Result> mproducts(@Query("userid") String userid);

    @GET("getprofile.php")
    Call<Result> userprofile(@Query("userid") String userid);

    @GET("getcategoryproducts.php")
    Call<Result> productcaegory(@Query("categoryid") String categoryid);

    @GET("getproductdetail.php")
    Call<Result> productdetails(@Query("productid") String productid);

    @GET("deleteproduct.php")
    Call<Result> productdelete(@Query("productid") String userid);

    @GET("getcategory.php")
    Call<Result> category();

    @FormUrlEncoded
    @POST("userupdate.php")
    Call<Result> userUpdate(@Field("userid") String id,
                            @Field("fullname") String fullname,
                            @Field("email") String email,
                            @Field("contact") String contact);

    @FormUrlEncoded
    @POST("usersignup.php")
    Call<Result> userSignup();
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
                                @Part MultipartBody.Part file4);
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
                                @Part MultipartBody.Part file5);
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
                                @Part MultipartBody.Part file6);

    ///////////update
    @Multipart
    @POST("userupdateproduct.php")
    Call<Result> updateProduct( @Part("productid") RequestBody id,
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
                                @Part MultipartBody.Part file6);

    @Multipart
    @POST("userupdateproduct.php")
    Call<Result> updateProduct( @Part("productid") RequestBody id,
                                @Part("categoryid") RequestBody categoryid,
                                @Part("productname") RequestBody productname,
                                @Part("price") RequestBody price,
                                @Part("description") RequestBody description,
                                @Part("location") RequestBody location,
                                @Part MultipartBody.Part file1,
                                @Part MultipartBody.Part file2,
                                @Part MultipartBody.Part file3,
                                @Part MultipartBody.Part file4);

    @Multipart
    @POST("userupdateproduct.php")
    Call<Result> updateProduct( @Part("productid") RequestBody id,
                                @Part("categoryid") RequestBody categoryid,
                                @Part("productname") RequestBody productname,
                                @Part("price") RequestBody price,
                                @Part("description") RequestBody description,
                                @Part("location") RequestBody location,
                                @Part MultipartBody.Part file1,
                                @Part MultipartBody.Part file2,
                                @Part MultipartBody.Part file3,
                                @Part MultipartBody.Part file4,
                                @Part MultipartBody.Part file5);

    @Multipart
    @POST("userupdateproduct.php")
    Call<Result> updateProduct(@Part("productid") RequestBody id,
                               @Part("categoryid") RequestBody categoryid,
                               @Part("productname") RequestBody productname,
                               @Part("price") RequestBody price,
                               @Part("description") RequestBody description,
                               @Part("location") RequestBody location);
}


