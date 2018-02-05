package com.buah.farmconnect.api;

import com.buah.farmconnect.object.ObjectProduct;
import com.buah.farmconnect.object.ObjectUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private ObjectUser objectUser;

    @SerializedName("product")
    private  ObjectProduct objectProductdetail;

    @SerializedName("products")
    private ArrayList<ObjectProduct> objectProducts;

    @SerializedName("tubers")
    private ArrayList<ObjectProduct> objectProductTubers;

    @SerializedName("fruits")
    private ArrayList<ObjectProduct> objectProductFruits;

    @SerializedName("vegetables")
    private ArrayList<ObjectProduct> objectProductVegetables;

    @SerializedName("grains")
    private ArrayList<ObjectProduct> objectProductGrains;

    @SerializedName("diaryfish")
    private ArrayList<ObjectProduct> objectProductDairyFish;
//
//    @SerializedName("products")
//    private ArrayList<ObjectProduct> objectProducts;
//
//    @SerializedName("products")
//    private ArrayList<ObjectProduct> objectProducts;
//
//    @SerializedName("products")
//    private ArrayList<ObjectProduct> objectProducts;
//
//    @SerializedName("products")
//    private ArrayList<ObjectProduct> objectProducts;


    public Result(Boolean error, String message, ObjectUser objectUser) {
        this.error = error;
        this.message = message;
        this.objectUser = objectUser;
    }

    public Result(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ObjectUser getObjectUser() {
        return objectUser;
    }

    public ObjectProduct getObjectProductdetail() {
        return objectProductdetail;
    }

    public ArrayList<ObjectProduct> getObjectProducts() {
        return objectProducts;
    }

    public ArrayList<ObjectProduct> getObjectProductTubers() {
        return objectProductTubers;
    }

    public ArrayList<ObjectProduct> getObjectProductFruits() {
        return objectProductFruits;
    }

    public ArrayList<ObjectProduct> getObjectProductVegetables() {
        return objectProductVegetables;
    }

    public ArrayList<ObjectProduct> getObjectProductGrains() {
        return objectProductGrains;
    }

    public ArrayList<ObjectProduct> getObjectProductDairyFish() {
        return objectProductDairyFish;
    }

}
