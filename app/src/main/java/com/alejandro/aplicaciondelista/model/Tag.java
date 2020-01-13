package com.alejandro.aplicaciondelista.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

    private int id;
    private String tag;

    public Tag(String tag) {
        this.id = -1;
        this.tag = tag;
    }

    protected Tag(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    protected Tag(Parcel in) {
        id = in.readInt();
        tag = in.readString();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(tag);
    }
}
