package com.example.myworkout.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myworkout.Utilities.CustomLatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout implements Parcelable {

    private String type;
    private long duration;
    private double distance;
    private double pace;
    private Date startTime;
    private double caloriesBurnt;
    private List<CustomLatLng> pathPoints;

    public Workout() {
        // Required for Firebase
    }

    public Workout(String type, Date startTime, double pace, double distance, long duration, double caloriesBurnt) {
        this.type = type;
        this.startTime = startTime;
        this.pace = pace;
        this.distance = distance;
        this.duration = duration;
        this.caloriesBurnt = caloriesBurnt;
        this.pathPoints = new ArrayList<>();
    }

    protected Workout(Parcel in) {
        type = in.readString();
        startTime = new Date(in.readLong());
        pace = in.readDouble();
        distance = in.readDouble();
        duration = in.readLong();
        caloriesBurnt = in.readDouble();
        pathPoints = new ArrayList<>();
        in.readList(pathPoints, CustomLatLng.class.getClassLoader());
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public List<CustomLatLng> getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(List<CustomLatLng> pathPoints) {
        this.pathPoints = pathPoints;
    }

    @NonNull
    @Override
    public String toString() {
        return "Workout{" +
                "type='" + type + '\'' +
                ", duration='" + duration + '\'' +
                ", distance=" + distance +
                ", pace=" + pace +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeLong(startTime.getTime());
        dest.writeDouble(pace);
        dest.writeDouble(distance);
        dest.writeLong(duration);
        dest.writeDouble(caloriesBurnt);
        dest.writeList(pathPoints);
    }
}
