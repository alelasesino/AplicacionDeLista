package com.alejandro.aplicaciondelista.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.UUID;

/**
 * Clase modelo con los datos de un producto
 */
public class ItemProduct implements Parcelable {

    private String id;
    private String imageUrl;
    private String name;
    private String details;
    private double price;
    //private String[] tags;
    private Tag[] tags;
    private boolean favorite;

    public ItemProduct(){
        id = UUID.randomUUID().toString();
    }


    protected ItemProduct(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        details = in.readString();
        price = in.readDouble();
        tags = in.createTypedArray(Tag.CREATOR);
        favorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(details);
        dest.writeDouble(price);
        dest.writeTypedArray(tags, flags);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemProduct{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                /*", details='" + details + '\'' +*/
                ", price=" + price +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

}
