package com.platinumstudio.contacts;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    String name;

    @ColumnInfo
    @NonNull
    String email;

    @ColumnInfo
    @NonNull
    long number;

    @ColumnInfo
    @NonNull
    String image;

    public Contact(String name, long number, String email, String image) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(int phone_number) {
        this.number = phone_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}