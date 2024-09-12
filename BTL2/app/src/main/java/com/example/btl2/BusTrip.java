package com.example.btl2;

import android.os.Parcel;
import android.os.Parcelable;

public class BusTrip implements Parcelable {
    private int id;
    private String date;
    private String time;
    private int seatCount;

    // Constructor với tham số
    public BusTrip(int id, String date, String time, int seatCount) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.seatCount = seatCount;
    }

    // Constructor mặc định
    public BusTrip() {
    }

    // Constructor để đọc từ Parcel
    protected BusTrip(Parcel in) {
        id = in.readInt();
        date = in.readString();
        time = in.readString();
        seatCount = in.readInt();
    }

    public static final Creator<BusTrip> CREATOR = new Creator<BusTrip>() {
        @Override
        public BusTrip createFromParcel(Parcel in) {
            return new BusTrip(in);
        }

        @Override
        public BusTrip[] newArray(int size) {
            return new BusTrip[size];
        }
    };

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeInt(seatCount);
    }
}
