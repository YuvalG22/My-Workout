package com.example.myworkout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myworkout.Intefaces.OnWorkoutDataUpdateListener;
import com.example.myworkout.Utilities.TimeFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkoutInfoFragment extends Fragment {
    private static final long DELAY = 1000L;
    private long elapsedTime = 0;
    private long startTime;
    private OnWorkoutDataUpdateListener dataUpdateListener;
    final Handler handler = new Handler();
    private boolean timerOn = false;
    private double caloriesBurnt;
    private double weight;
    private double height;
    private MaterialTextView main_LBL_time;
    private MaterialTextView main_LBL_dist;
    private MaterialTextView main_LBL_pace;
    private MaterialTextView main_LBL_calories;
    private MaterialCardView card_dist;
    private MaterialCardView card_pace2;
    private String workoutType;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            updateTime();
            updateCalories();
        }
    };

    public WorkoutInfoFragment() {
        // Required empty public constructor
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnWorkoutDataUpdateListener) {
            dataUpdateListener = (OnWorkoutDataUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnWorkoutDataUpdateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout_info, container, false);
        if (getArguments() != null) {
            workoutType = getArguments().getString("type");
        }
        findViews(v);
        if (workoutType.equals("Gym")) {
            card_dist.setVisibility(View.GONE);
            card_pace2.setVisibility(View.GONE);
        }
        loadUserData();
        return v;
    }

    public void stopTimer() {
        timerOn = false;
        handler.removeCallbacks(runnable);
    }

    public void startTimer() {
        if (!timerOn) {
            startTime = System.currentTimeMillis() - elapsedTime;
            handler.postDelayed(runnable, 0);
            timerOn = true;
        }
    }

    private void updateTime(){
        long currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - startTime;
        main_LBL_time.setText(TimeFormatter.formatTime(elapsedTime));

        if (dataUpdateListener != null) {
            dataUpdateListener.onTimeUpdated(elapsedTime);
        }
    }

    private void updateCalories() {
        double durationInMinutes = elapsedTime / 60000.0;
        caloriesBurnt = calculateCalories(workoutType, durationInMinutes, weight);
        int roundedCalories = (int) Math.round(caloriesBurnt);
        main_LBL_calories.setText(String.format("%d ", roundedCalories));
    }

    public void updateDistance(double distance) {
        main_LBL_dist.setText(String.format("%.2f ", distance / 1000));
    }

    public void updatePace(double pace) {
        main_LBL_pace.setText(String.format("%.2f ", pace / 60));
    }

    public String getDuration(){
        return main_LBL_time.toString();
    }

    private void findViews(View v){
        main_LBL_time = v.findViewById(R.id.main_LBL_time);
        main_LBL_dist = v.findViewById(R.id.main_LBL_dist);
        main_LBL_pace = v.findViewById(R.id.main_LBL_pace);
        main_LBL_calories = v.findViewById(R.id.main_LBL_calories);
        card_dist = v.findViewById(R.id.card_dist);
        card_pace2 = v.findViewById(R.id.card_pace2);
    }

    private double calculateCalories(String workoutType, double durationInMinutes, double weightInKg) {
        double caloriesBurntPerMinute;
        switch (workoutType.toLowerCase()) {
            case "running":
                caloriesBurntPerMinute = 0.0175 * 10 * weightInKg;
                break;
            case "cycling":
                caloriesBurntPerMinute = 0.0175 * 8 * weightInKg;
                break;
            case "walking":
                caloriesBurntPerMinute = 0.0175 * 3.5 * weightInKg;
                break;
            case "gym":
                caloriesBurntPerMinute = 0.0175 * 6 * weightInKg;
                break;
            default:
                caloriesBurntPerMinute = 0.0175 * 5 * weightInKg;
                break;
        }
        return caloriesBurntPerMinute * durationInMinutes;
    }

    public double getCaloriesBurnt() {
        return caloriesBurnt;
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String weightStr = snapshot.child("weight").getValue(String.class);
                    String heightStr = snapshot.child("height").getValue(String.class);
                    if (weightStr != null && !weightStr.isEmpty()) {
                        weight = Double.parseDouble(weightStr);
                    } else {
                        weight = 75.0;
                    }

                    if (heightStr != null && !heightStr.isEmpty()) {
                        height = Double.parseDouble(heightStr);
                    } else {
                        height = 175.0;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public static WorkoutInfoFragment newInstance(String workoutType) {
        WorkoutInfoFragment fragment = new WorkoutInfoFragment();
        Bundle args = new Bundle();
        args.putString("type", workoutType);
        fragment.setArguments(args);
        return fragment;
    }
}