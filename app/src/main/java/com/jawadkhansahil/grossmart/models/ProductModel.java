package com.jawadkhansahil.grossmart.models;

public class ProductModel {
    String productID;
    String productImage;
    String productName;
    String productWeight;
    String productPrice;
    String productCalories;
    String productDescription;
    String deliveryTime;


    public ProductModel() {
    }

    public ProductModel(String productID, String productImage, String productName, String productWeight, String productPrice, String productCalories, String productDescription, String deliveryTime) {
        this.productID = productID;
        this.productImage = productImage;
        this.productName = productName;
        this.productWeight = productWeight;
        this.productPrice = productPrice;
        this.productCalories = productCalories;
        this.productDescription = productDescription;
        this.deliveryTime = deliveryTime;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCalories() {
        return productCalories;
    }

    public void setProductCalories(String productCalories) {
        this.productCalories = productCalories;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
