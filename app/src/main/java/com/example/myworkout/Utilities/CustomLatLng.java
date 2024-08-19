package com.example.myworkout.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class CustomLatLng implements Parcelable {
    private double latitude;
    private double longitude;

    // No-argument constructor
    public CustomLatLng() {
        // Required for Firebase
    }

    // Constructor with arguments
    public CustomLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected CustomLatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<CustomLatLng> CREATOR = new Creator<CustomLatLng>() {
        @Override
        public CustomLatLng createFromParcel(Parcel in) {
            return new CustomLatLng(in);
        }

        @Override
        public CustomLatLng[] newArray(int size) {
            return new CustomLatLng[size];
        }
    };

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Convert to Google Maps LatLng
    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    // Convert from Google Maps LatLng
    public static CustomLatLng fromLatLng(LatLng latLng) {
        return new CustomLatLng(latLng.latitude, latLng.longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
