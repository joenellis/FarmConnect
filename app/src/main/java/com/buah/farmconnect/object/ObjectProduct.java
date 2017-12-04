package com.buah.farmconnect.object;

import com.google.gson.annotations.SerializedName;

public class ObjectProduct {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("productname")
    private String productname;

    @SerializedName("price")
    private String price;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("image")
    private String image;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("video")
    private String video;

    @SerializedName("audio")
    private String audio;

    @SerializedName("categoryname")
    private String categoryname;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("contact")
    private String contact;

    public ObjectProduct() {
    }

    public ObjectProduct(String product_id, String name, String price, String location, String image) {

        this.product_id = product_id;
        this.productname = name;
        this.price = price;
        this.location = location;
        this.image = image;
    }

    public ObjectProduct(String product_id, String name, String price, String image) {
        this.product_id = product_id;
        this.productname = name;
        this.price = price;
        this.image = image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getProductname() {
        return productname;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getContact() {
        return contact;
    }


}
