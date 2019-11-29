package com.alejandro.aplicaciondelista.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class ItemProduct implements Parcelable {

    private String id;
    private String imageUrl;
    private String name;
    private String details;
    private double price;
    private String[] tags;

    public ItemProduct(){}

    public ItemProduct(String id, String imageUrl, String name, String details, double price, String[] tags) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.details = details;
        this.price = price;
        this.tags = tags;
    }

    protected ItemProduct(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        details = in.readString();
        price = in.readDouble();
        tags = in.createStringArray();
    }

    public static final Creator<ItemProduct> CREATOR = new Creator<ItemProduct>() {
        @Override
        public ItemProduct createFromParcel(Parcel in) {
            return new ItemProduct(in);
        }

        @Override
        public ItemProduct[] newArray(int size) {
            return new ItemProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageUrl);
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeDouble(price);
        parcel.writeStringArray(tags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ItemProduct{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}