package com.platinumstudio.contacts.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("title")
    private String title;


    public Photo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
