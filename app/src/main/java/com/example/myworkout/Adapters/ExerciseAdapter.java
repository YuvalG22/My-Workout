package com.example.myworkout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.ExerciseDetailActivity;
import com.example.myworkout.Models.Exercise;
import com.example.myworkout.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exercises;
    private Context context;

    public ExerciseAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.main_LBL_exercise.setText(exercise.getName());

        holder.exercise_card.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("EXERCISES", exercise);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return exercises == null ? 0 : exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView main_LBL_exercise;
        MaterialCardView exercise_card;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            main_LBL_exercise = itemView.findViewById(R.id.main_LBL_exercise);
            exercise_card = itemView.findViewById(R.id.exercise_card);
        }
    }
}
