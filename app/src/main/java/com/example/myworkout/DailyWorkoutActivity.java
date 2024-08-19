package com.example.myworkout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.Adapters.WorkoutAdapter;
import com.example.myworkout.Models.Workout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DailyWorkoutActivity extends ToolbarActivity {
    private ArrayList<Workout> todayWorkouts = new ArrayList<>();
    private ArrayList<Workout> weeklyWorkouts = new ArrayList<>();
    private RecyclerView main_LST_daily;
    private MaterialTextView main_LBL_no_workouts;
    private MaterialTextView main_LBL_daily_weekly_title;
    private WorkoutAdapter workoutAdapter;
    private boolean isWeekly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_workout);
        setupToolbar(R.id.toolbar_w, true);
        isWeekly = getIntent().getBooleanExtra("IS_WEEKLY", false);
        findViews();
        initViews();
    }

    private void initViews() {
        if(isWeekly){
            getSupportActionBar().setTitle("Weekly Activity");
            weeklyWorkouts = getIntent().getParcelableArrayListExtra("WEEK_WORKOUTS");
            workoutAdapter = new WorkoutAdapter(this, weeklyWorkouts);
            if(weeklyWorkouts.isEmpty()) {
                main_LBL_no_workouts.setVisibility(View.VISIBLE);
            }
        }
        else{
            getSupportActionBar().setTitle("Daily Activity");
            todayWorkouts = getIntent().getParcelableArrayListExtra("TODAY_WORKOUTS");
            workoutAdapter = new WorkoutAdapter(this, todayWorkouts);
            if(todayWorkouts.isEmpty()){
                main_LBL_no_workouts.setVisibility(View.VISIBLE);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_daily.setLayoutManager(linearLayoutManager);
        main_LST_daily.setAdapter(workoutAdapter);
    }

    private void findViews(){
        main_LST_daily = findViewById(R.id.main_LST_daily);
        main_LBL_no_workouts = findViewById(R.id.main_LBL_no_workouts);
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