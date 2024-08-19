package com.example.myworkout.Intefaces;

public interface OnWorkoutDataUpdateListener {
    void onDistanceUpdated(double distance);
    void onPaceUpdated(double pace);
    void onTimeUpdated(long elapsedTime);
}
