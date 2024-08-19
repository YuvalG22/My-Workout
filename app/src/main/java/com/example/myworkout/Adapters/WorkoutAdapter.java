package com.example.myworkout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.Utilities.DateFormatter;
import com.example.myworkout.Models.Workout;
import com.example.myworkout.WorkoutSummary;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private final ArrayList<Workout> workouts;
    private Context context;

    public WorkoutAdapter(Context context, ArrayList<Workout> workouts){
        this.workouts = workouts;
        this.context = context;
    }
    @NonNull
    @Override
    public WorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutViewHolder holder, int position) {
        Workout workout = getItem(position);
        holder.main_IMG_work_icon.setImageResource(getWorkoutIcon(workout.getType()));
        int color = getWorkoutColor(workout.getType());
        holder.main_IMG_work_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        holder.main_LBL_work_type.setText(workout.getType());
        holder.main_LBL_work_type.setTextColor(color);
        String formattedDate = DateFormatter.formatToDayMonthTime(workout.getStartTime());
        holder.main_LBL_work_date.setText(formattedDate);
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WorkoutSummary.class);
            intent.putExtra("WORKOUT", workout);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workouts == null ? 0 : workouts.size();
    }

    public Workout getItem(int position) {
        return workouts.get(position);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        private final ShapeableImageView main_IMG_work_icon;
        private final MaterialTextView main_LBL_work_type;
        private final MaterialTextView main_LBL_work_date;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.workout_card);
            main_IMG_work_icon = itemView.findViewById(R.id.main_IMG_work_icon);
            main_LBL_work_type = itemView.findViewById(R.id.main_LBL_work_type);
            main_LBL_work_date = itemView.findViewById(R.id.main_LBL_work_date);
        }
    }

    private int getWorkoutIcon(String workoutType) {
        switch (workoutType.toLowerCase()) {
            case "running":
                return R.drawable.speed;
            case "cycling":
                return R.drawable.bicycle;
            case "walking":
                return R.drawable.walking;
            case "gym":
                return R.drawable.weightlifting;
            default:
                return R.drawable.speed;
        }
    }

    private int getWorkoutColor(String workoutType) {
        switch (workoutType.toLowerCase()) {
            case "running":
                return ContextCompat.getColor(context, R.color.dodger_blue);
            case "cycling":
                return ContextCompat.getColor(context, R.color.dark_orange);
            case "walking":
                return ContextCompat.getColor(context, R.color.spring_green);
            case "gym":
                return ContextCompat.getColor(context, R.color.yellow);
            default:
                return ContextCompat.getColor(context, R.color.black);
        }
    }
}
