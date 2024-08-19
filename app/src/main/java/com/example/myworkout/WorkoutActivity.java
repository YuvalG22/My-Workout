package com.example.myworkout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.myworkout.Intefaces.OnWorkoutDataUpdateListener;
import com.example.myworkout.Models.Workout;
import com.example.myworkout.Utilities.CustomLatLng;
import com.example.myworkout.Utilities.ToastVibrate;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class WorkoutActivity extends ToolbarActivity implements OnWorkoutDataUpdateListener {
    private WorkoutInfoFragment infoFragment;
    private MapFragment mapFragment;
    private FrameLayout main_FRAME_info;
    private FrameLayout main_FRAME_map;
    private MaterialButton main_BTN_start;
    private MaterialButton main_BTN_stop;
    private MaterialButton main_BTN_resume;
    private MaterialButton main_BTN_pause;
    private String type;
    private ToastVibrate toastVibrate;
    private boolean isWorkoutStarted = false;
    private boolean isMapRequired;
    private float totalDistance = 0;
    private Location lastLocation;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle();
        isMapRequired = !type.equals("Gym");

        setupToolbar(R.id.toolbar_w, true);
        getSupportActionBar().setTitle(type);

        findViews();
        initViews();
    }

    private void setTitle() {
        Intent previousActivity = getIntent();
        type = previousActivity.getStringExtra("WORKOUT_TYPE");
    }

    private void initViews() {
        toastVibrate = new ToastVibrate(this);
        infoFragment = WorkoutInfoFragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_info, infoFragment).commit();
        if(isMapRequired) {
            mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
        }
        else{
            main_FRAME_map.setVisibility(View.GONE);
        }
        main_BTN_start.setOnClickListener(v -> onStartWorkout());
        main_BTN_pause.setOnClickListener(v -> onPauseWorkout());
        main_BTN_resume.setOnClickListener(v -> onResumeWorkout());
        main_BTN_stop.setOnClickListener(v -> onStopWorkout());
        main_BTN_pause.setVisibility(View.INVISIBLE);
        main_BTN_resume.setVisibility(View.INVISIBLE);
        main_BTN_start.setVisibility(View.VISIBLE);
        main_BTN_stop.setVisibility(View.INVISIBLE);
    }

    private void onStopWorkout() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double distance = 0;
        double pace = 0;
        double caloriesBurnt = infoFragment.getCaloriesBurnt();
        List<CustomLatLng> pathPoints = null;
        if (isMapRequired) {
            distance = mapFragment.getTotalDistance();
            pace = mapFragment.getPace();
            pathPoints = mapFragment.getPathPoints();
        }
        Workout workout = new Workout(type, new Date(startTime), pace, distance, duration, caloriesBurnt);
        if (isMapRequired) {
            workout.setPathPoints(pathPoints);
        }
        saveWorkoutData(workout);
        toastVibrate.toastAndVibrate("Workout Saved", 200);
        transactToWorkoutSummary(workout);
    }

    private void transactToWorkoutSummary(Workout workout) {
        Intent intent = new Intent(this, WorkoutSummary.class);
        intent.putExtra("WORKOUT", workout);
        startActivity(intent);
        finish();
    }

    private void saveWorkoutData(Workout workout) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference workoutsRef = database.getReference("users").child(uid).child("workouts");
            String workoutId = workoutsRef.push().getKey();
            workoutsRef.child(workoutId).setValue(workout);
        }
    }

    private void onResumeWorkout() {
        main_BTN_pause.setVisibility(View.VISIBLE);
        main_BTN_stop.setVisibility(View.INVISIBLE);
        main_BTN_resume.setVisibility(View.INVISIBLE);
        infoFragment.startTimer();
        if (isMapRequired) {
            mapFragment.startTracking();
        }
    }

    private void onPauseWorkout() {
        main_BTN_pause.setVisibility(View.INVISIBLE);
        main_BTN_stop.setVisibility(View.VISIBLE);
        main_BTN_resume.setVisibility(View.VISIBLE);
        infoFragment.stopTimer();
        if (isMapRequired) {
            mapFragment.stopTracking();
        }
    }

    private void onStartWorkout() {
        startTime = System.currentTimeMillis();
        main_BTN_pause.setVisibility(View.VISIBLE);
        main_BTN_start.setVisibility(View.INVISIBLE);
        infoFragment.startTimer();
        if (isMapRequired) {
            mapFragment.startTracking();
        }
    }

    private void findViews() {
        main_FRAME_info = findViewById(R.id.main_FRAME_info);
        main_FRAME_map = findViewById(R.id.main_FRAME_map);
        main_BTN_start = findViewById(R.id.main_BTN_start);
        main_BTN_stop = findViewById(R.id.main_BTN_stop);
        main_BTN_resume = findViewById(R.id.main_BTN_resume);
        main_BTN_pause = findViewById(R.id.main_BTN_pause);
    }

    @Override
    public void onDistanceUpdated(double distance) {
        infoFragment.updateDistance(distance);
    }

    @Override
    public void onPaceUpdated(double pace) {
        infoFragment.updatePace(pace);
    }

    @Override
    public void onTimeUpdated(long elapsedTime) {
        if (isMapRequired) {
            mapFragment.updateElapsedTime(elapsedTime);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button action
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}