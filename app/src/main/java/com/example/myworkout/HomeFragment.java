package com.example.myworkout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myworkout.Models.Workout;
import com.example.myworkout.Utilities.DateFormatter;
import com.example.myworkout.Utilities.SharedPreferencesManager;
import com.example.myworkout.Utilities.TimeFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements SensorEventListener {
    private ArrayList<Workout> todayWorkouts = new ArrayList<>();
    private ArrayList<Workout> weeklyWorkouts = new ArrayList<>();
    private FloatingActionButton main_FAB_run;
    private FloatingActionButton main_FAB_walk;
    private FloatingActionButton main_FAB_cycle;
    private FloatingActionButton main_FAB_gym;
    private MaterialCardView main_CRD_daily;
    private MaterialCardView main_CRD_weekly;
    private MaterialCardView card_steps;
    private TextView main_LBL_daily_duration;
    private TextView main_LBL_weekly_duration;
    private TextView main_LBL_daily_calories;
    private TextView main_LBL_steps_count;
    private TextView main_LBL_steps_goal;
    private TextView main_LBL_steps_percent;
    private TextView steps_message;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isSensorPresent;
    private int stepCount = 0;
    private int stepGoal = 5000;
    private ProgressBar stepsProgressBar;
    private int initialStepCount = 0;
    private boolean isInitialStepCountSet = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(v);
        loadStepGoal();
        checkActivityRecognitionPermission();
        initializeStepCounter();
        String lastSavedDate = SharedPreferencesManager.getInstance().getString("lastSavedDate","");
        String currentDate = DateFormatter.formatToDayMonthYear(new Date());
        if (lastSavedDate == null || lastSavedDate.isEmpty() || !currentDate.equals(lastSavedDate)) {
            SharedPreferencesManager.getInstance().putString("lastSavedDate", currentDate);
            SharedPreferencesManager.getInstance().putInt("initialStepCount", 0);
            stepsProgressBar.setProgress(0);
            main_LBL_steps_percent.setText("0%");
            isInitialStepCountSet = false;
        } else {
            initialStepCount = SharedPreferencesManager.getInstance().getInt("initialStepCount", 0);
            isInitialStepCountSet = true;
        }
        loadDailyWorkouts();
        initViews();
        return v;
    }

    private void initViews() {
        main_FAB_run.setOnClickListener(v -> onWorkoutClick("Running"));
        main_FAB_walk.setOnClickListener(v -> onWorkoutClick("Walking"));
        main_FAB_cycle.setOnClickListener(v -> onWorkoutClick("Cycling"));
        main_FAB_gym.setOnClickListener(v -> onWorkoutClick("Gym"));
        main_CRD_daily.setOnClickListener(v -> transactToDaily(false));
        main_CRD_weekly.setOnClickListener(v -> transactToDaily(true));
        card_steps.setOnClickListener(v -> showChangeGoalDialog());
    }

    private void showChangeGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Set Daily Step Goal");
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newGoal = input.getText().toString();
            stepGoal = Integer.parseInt(newGoal);
            if (!newGoal.isEmpty()) {
                saveStepGoal(stepGoal);
                updateStepGoalUI(stepGoal);
                if (stepCount >= stepGoal) {
                    steps_message.setVisibility(View.VISIBLE);
                } else {
                    steps_message.setVisibility(View.GONE);
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateStepGoalUI(int goal) {
        main_LBL_steps_goal.setText(String.format("%d", goal));
        stepsProgressBar.setMax(stepGoal);
        stepsProgressBar.setProgress(stepCount);
        int percentage = (int) ((stepCount / (float) goal) * 100);
        main_LBL_steps_percent.setText(String.format("%d%%", percentage));
    }

    private void saveStepGoal(int goal) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.child("daily_step_goal").setValue(goal);
        }
    }

    private void loadStepGoal() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.child("daily_step_goal").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer goal = snapshot.getValue(Integer.class);
                    if (goal != null) {
                        stepGoal = goal;
                        updateStepGoalUI(stepGoal);
                    } else {
                        saveStepGoal(5000); // Default goal if not set
                        updateStepGoalUI(5000);
                    }
                    if (stepCount >= stepGoal) {
                        steps_message.setVisibility(View.VISIBLE);
                    } else {
                        steps_message.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors
                }
            });
        }
    }

    private void loadDailyWorkouts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("workouts");
            workoutsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> lastSevenDays = getLastSevenDays();
                    long dailyDuration = 0;
                    long weeklyDuration = 0;
                    int dailyCalories = 0;
                    todayWorkouts.clear();
                    weeklyWorkouts.clear();
                    Date currentDate = getCurrentDate();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Workout workout = snapshot.getValue(Workout.class);
                        String workoutDate = DateFormatter.formatToDayMonthYear(workout.getStartTime());
                        String formattedCurrentDate = DateFormatter.formatToDayMonthYear(currentDate);
                        if (workoutDate.equals(formattedCurrentDate)) {
                            todayWorkouts.add(workout);
                            dailyDuration += workout.getDuration();
                            dailyCalories += (int) Math.round(workout.getCaloriesBurnt());
                        }

                        if (lastSevenDays.contains(workoutDate)) {
                            weeklyWorkouts.add(workout);
                            weeklyDuration += workout.getDuration();
                        }

                    }
                    main_LBL_daily_duration.setText(TimeFormatter.formatTime(dailyDuration));
                    main_LBL_weekly_duration.setText(TimeFormatter.formatTime(weeklyDuration));
                    main_LBL_daily_calories.setText(String.valueOf(dailyCalories));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void transactToDaily(boolean isWeekly) {
        Intent intent = new Intent(getActivity(), DailyWorkoutActivity.class);
        intent.putParcelableArrayListExtra("WEEK_WORKOUTS", weeklyWorkouts);
        intent.putParcelableArrayListExtra("TODAY_WORKOUTS", todayWorkouts);
        intent.putExtra("IS_WEEKLY", isWeekly);
        startActivity(intent);
    }

    private void onWorkoutClick(String workout) {
        Intent intent = new Intent(getActivity(), WorkoutActivity.class);
        intent.putExtra("WORKOUT_TYPE", workout);
        startActivity(intent);
    }

    private void findViews(View v) {
        main_FAB_run = v.findViewById(R.id.quick_FAB_run);
        main_FAB_walk = v.findViewById(R.id.quick_FAB_walk);
        main_FAB_cycle = v.findViewById(R.id.quick_FAB_cycle);
        main_FAB_gym = v.findViewById(R.id.quick_FAB_gym);
        main_CRD_daily = v.findViewById(R.id.main_CRD_daily);
        main_CRD_weekly = v.findViewById(R.id.main_CRD_weekly);
        card_steps = v.findViewById(R.id.card_steps);
        main_LBL_daily_duration = v.findViewById(R.id.main_LBL_daily_duration);
        main_LBL_weekly_duration = v.findViewById(R.id.main_LBL_weekly_duration);
        main_LBL_daily_calories = v.findViewById(R.id.main_LBL_daily_calories);
        main_LBL_steps_count = v.findViewById(R.id.main_LBL_steps_count);
        main_LBL_steps_goal = v.findViewById(R.id.main_LBL_steps_goal);
        main_LBL_steps_percent = v.findViewById(R.id.main_LBL_steps_percent);
        steps_message = v.findViewById(R.id.steps_message);
        stepsProgressBar = v.findViewById(R.id.steps_progress);
    }

    private Date getCurrentDate() {
        return new Date();
    }

    private ArrayList<String> getLastSevenDays() {
        ArrayList<String> lastSevenDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            lastSevenDays.add(DateFormatter.formatToDayMonthYear(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        return lastSevenDays;
    }

    private void initializeStepCounter() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
            main_LBL_steps_count.setText("Step Counter Sensor not available!");
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) event.values[0];
            if (!isInitialStepCountSet) {
                initialStepCount = totalSteps;
                SharedPreferencesManager.getInstance().putInt("initialStepCount", initialStepCount);
                isInitialStepCountSet = true;
            }
            // Calculate the steps taken today
            stepCount = totalSteps - initialStepCount;
            main_LBL_steps_count.setText(String.valueOf(stepCount));
            stepsProgressBar.setMax(stepGoal);
            stepsProgressBar.setProgress(stepCount);
            int percentage = (int) ((stepCount / (float) stepGoal) * 100);
            main_LBL_steps_percent.setText(String.format("%d%%", percentage));

            if (stepCount >= stepGoal) {
                steps_message.setVisibility(View.VISIBLE);
            } else {
                steps_message.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    private void checkActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(requireContext(), "android.permission.ACTIVITY_RECOGNITION")
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{"android.permission.ACTIVITY_RECOGNITION"}, 1);
            } else {
                initializeStepCounter(); // Permission already granted, initialize step counter
            }
        } else {
            initializeStepCounter(); // For devices below Android 10
        }
    }
}