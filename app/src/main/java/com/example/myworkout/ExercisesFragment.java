package com.example.myworkout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

public class ExercisesFragment extends Fragment {
    private MaterialCardView card_chest;
    private MaterialCardView card_back;
    private MaterialCardView card_abs;
    private MaterialCardView card_shoulders;
    private MaterialCardView card_biceps;
    private MaterialCardView card_triceps;
    private MaterialCardView card_legs;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercises, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void initViews() {
        card_chest.setOnClickListener(v -> transactToExercises("Chest"));
        card_back.setOnClickListener(v -> transactToExercises("Back"));
        card_shoulders.setOnClickListener(v -> transactToExercises("Shoulders"));
        card_biceps.setOnClickListener(v -> transactToExercises("Biceps"));
        card_triceps.setOnClickListener(v -> transactToExercises("Triceps"));
        card_abs.setOnClickListener(v -> transactToExercises("Abs"));
        card_legs.setOnClickListener(v -> transactToExercises("Legs"));
    }

    private void transactToExercises(String muscleGroup){
        Intent intent = new Intent(getActivity(), MuscleGroupActivity.class);
        intent.putExtra("MUSCLE_GROUP", muscleGroup);
        startActivity(intent);
    }

    private void findViews(View v) {
        card_chest = v.findViewById(R.id.card_chest);
        card_back = v.findViewById(R.id.card_back);
        card_abs = v.findViewById(R.id.card_abs);
        card_shoulders = v.findViewById(R.id.card_shoulders);
        card_biceps = v.findViewById(R.id.card_biceps);
        card_triceps = v.findViewById(R.id.card_triceps);
        card_legs = v.findViewById(R.id.card_legs);

    }
}