package com.jawadkhansahil.grossmart.models;

public class CategoryModel {
    String id;
    String image;
    String category;

    public CategoryModel() {
    }

    public CategoryModel(String id, String image, String category) {
        this.id = id;
        this.image = image;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
