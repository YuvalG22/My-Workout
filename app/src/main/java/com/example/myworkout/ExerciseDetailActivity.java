package com.example.myworkout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myworkout.Models.Exercise;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class ExerciseDetailActivity extends ToolbarActivity {
    private MaterialTextView main_LBL_exercise_name;
    private MaterialTextView main_LBL_desc;
    private ShapeableImageView main_IMG_exercise;
    private Exercise exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        exercise = getIntent().getParcelableExtra("EXERCISES");

        setupToolbar(R.id.toolbar_w, true);
        getSupportActionBar().setTitle(exercise.getName());

        findViews();
        initViews();
    }

    private void initViews() {
        main_LBL_exercise_name.setText(exercise.getName());
        main_LBL_desc.setText(exercise.getDescription());
        main_IMG_exercise.setImageResource(exercise.getImageResourceId());
    }

    private void findViews() {
        main_LBL_exercise_name = findViewById(R.id.main_LBL_exercise_name);
        main_LBL_desc = findViewById(R.id.main_LBL_desc);
        main_IMG_exercise = findViewById(R.id.main_IMG_exercise);
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