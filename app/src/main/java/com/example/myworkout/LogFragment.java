package com.example.myworkout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myworkout.Adapters.WorkoutAdapter;
import com.example.myworkout.Models.Workout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogFragment extends Fragment {
    private ArrayList<Workout> workoutList = new ArrayList<>();
    private RecyclerView main_LST_workouts;
    private WorkoutAdapter workoutAdapter;
    private MaterialTextView main_LBL_empty_workouts;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log, container, false);
        findViews(v);
        loadWorkouts();
        initViews();
        return v;
    }

    private void loadWorkouts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            FirebaseDatabase.getInstance().getReference("users").child(uid).child("workouts")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            workoutList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Workout workout = snapshot.getValue(Workout.class);
                                if (workout != null) {
                                    workoutList.add(workout);
                                }
                            }
                            if (workoutList.isEmpty()) {
                                main_LBL_empty_workouts.setVisibility(View.VISIBLE);
                            }
                            workoutAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void initViews() {
        workoutAdapter = new WorkoutAdapter(getContext(), workoutList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_workouts.setLayoutManager(linearLayoutManager);
        main_LST_workouts.setAdapter(workoutAdapter);
    }

    private void findViews(View v) {
        main_LST_workouts = v.findViewById(R.id.main_LST_workouts);
        main_LBL_empty_workouts = v.findViewById(R.id.main_LBL_empty_workouts);
    }
}