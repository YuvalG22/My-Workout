package com.example.myworkout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.Adapters.ExerciseAdapter;
import com.example.myworkout.Adapters.WorkoutAdapter;
import com.example.myworkout.Models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroupActivity extends ToolbarActivity {
    private RecyclerView main_LST_exercise;
    private ExerciseAdapter exerciseAdapter;
    private ArrayList<Exercise> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_group);

        String muscleGroup = getIntent().getStringExtra("MUSCLE_GROUP");

        setupToolbar(R.id.toolbar_w, true);
        getSupportActionBar().setTitle(muscleGroup + " Exercises");

        exerciseList = getExercisesForMuscleGroup(muscleGroup);
        findViews();
        initViews();
    }

    private void initViews() {
        exerciseAdapter = new ExerciseAdapter(this, exerciseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_exercise.setLayoutManager(linearLayoutManager);
        main_LST_exercise.setAdapter(exerciseAdapter);
    }

    private void findViews() {
        main_LST_exercise = findViewById(R.id.main_LST_exercise);
    }

    private ArrayList<Exercise> getExercisesForMuscleGroup(String muscleGroup) {
        ArrayList<Exercise> exercises = new ArrayList<>();

        switch (muscleGroup) {
            case "Chest":
                exercises.add(new Exercise("Bench Press", "Lying on the bench, your feet flat on the floor. Grasp the bar with a grip wider than shoulder width. Your forearms should be perpendicular to the floor.\nUnhook the bar and slowly lower it to the lower part of your chest. As you contract your pectorals, push the load upwards until your arms are almost straight.\nKeep your elbows on the outside to put maximum stress on your chest and minimum stress on your anterior deltoids and triceps.\n" +
                        "Keep your shoulders flat against the bench.", R.drawable.bench_press));
                exercises.add(new Exercise("Incline Dumbbell Press", "Lying on the sloping bench, your feet flat on the ground, one dumbbell in each hand, pronation grip. Hold the dumbbells at your sides at chest height. Your forearms should be perpendicular to the ground.\nWhile contracting your pectorals, push the load upwards until your arms are almost straight, then return to the initial position.\nKeep your elbows on the outside to put maximum stress on your pectorals and minimum stress on your anterior deltoids and triceps.\n" +
                        "Keep your shoulders flat against the bench.", R.drawable.incline_press));
                exercises.add(new Exercise("Dumbbell Flys", "Lying on the bench, your feet flat on the floor, one dumbbell in each hand. Hold the weights in a neutral grip, arms outstretched above your chest.\nSlowly lower the dumbbells to the sides in an arc with your arms until you feel a stretch in your pectorals. Do not go below shoulder level. Then return to the original position, performing the same arc.\nKeep your elbows pointed outward and slightly bent to reduce stress on the joint.\n" +
                        "Keep your shoulders flat against the bench.", R.drawable.dumbbell_flys));
                break;

            case "Back":
                exercises.add(new Exercise("Lat Pulldown", "Sitting, thighs under the padded parts, the bar grasped in pronation, hands more apart than shoulder width. Keep elbows pointed outwards and back straight.\nPull the bar up to your chin. Keep the contraction for a moment before slowly returning to the initial position. During the movement, do not let your elbows come forward.", R.drawable.lat_pulldown));
                exercises.add(new Exercise("Seated Rows", "Sitting on the machine's bench with your feet on the wedge. Grasp the double handle attached to the cable with both hands. Keep back straight and knees slightly bent.\nPull the load towards your abdomen. Tighten your back at the end of the movement by bringing your elbows as far back as possible. Then slowly return to the initial position.", R.drawable.seated_row));
                exercises.add(new Exercise("Pullover", "Standing facing the high pulley, knees slightly bent and bust slightly bent forward. Grasp one end of the rope in each hand in a neutral grip.\nPull the rope up to your hips, letting your arms describe an arc of a circle. Keep your elbows slightly bent and close to your body. At the end of the movement, contract your back by squeezing your shoulders back, then return to the initial position.", R.drawable.pullover));
                break;

            case "Shoulders":
                exercises.add(new Exercise("Overhead Press", "Sitting on a bench with your back well supported against the backrest. Hold the dumbbells in your hands (pronation grip) a little above shoulder height. Your elbows are on the outside and your forearms are perpendicular to the ground.\nPush the dumbbells upwards. Until your arms are fully extended. Then return to the original position.", R.drawable.shoulder_press));
                exercises.add(new Exercise("Lateral Raise", "Standing with feet about shoulder width apart, arms along the body and a dumbbell in each hand.\nSlowly raise the arms sideways to the horizontal, keeping the elbows slightly bent. Contract your deltoids for a moment in the high position, then slowly return to the initial position.", R.drawable.lateral_raises));
                exercises.add(new Exercise("Rear Delt Flys", "Standing between the pulleys with the right handle in the left hand and the left handle in the right hand. Your arms folded in front of you at shoulder height.\nTightening your posterior deltoids and keeping your arms almost straight, pull your elbows as far back as possible in an arc. Stay in this position for a moment before returning to the initial position.", R.drawable.rear_delt));
                break;

            case "Biceps":
                exercises.add(new Exercise("Bicep Curls", "Standing with knees slightly bent and back straight. Hold a dumbbell in each hand, in a neutral grip along the body.\nWithout making a bust movement, raise a dumbbell by bending the forearm. During the movement, rotate your wrist outwards until your hand is supine in the upright position. Contract your biceps, then slowly return to the initial position. Keep your elbow close to your body during the movement. Alternate this movement one arm after the other.", R.drawable.dumbbell_curl));
                exercises.add(new Exercise("Hammer Curls", "Standing with knees slightly bent and back straight. Hold a dumbbell in each hand, in a neutral grip along the body.\nWithout making a bust movement, raise a dumbbell by bending the forearms. Keep your hand in a neutral grip (hence the name hammer grip). Contract your biceps, then slowly return to the initial position. Keep your elbow close to your body during the movement. Alternate this movement one arm after the other.", R.drawable.hammer_curl));
                break;

            case "Triceps":
                exercises.add(new Exercise("Triceps Press", "Standing facing the pulley, grasping the bar with a pronation grip, hands shoulder width apart.\nCarry out an extension of the forearms until your arms are fully extended. In this position, contract your triceps for a moment and then slowly return to the initial position. Keep your elbows close to your body during the whole movement.", R.drawable.tricep_press));
                exercises.add(new Exercise("Lateral Raise", "Lying on the bench, your feet flat on the floor or on the bench. Hold the EZ bar above your chest, pronated grip, hands slightly tighter than shoulder width.\nSlowly bend your forearms without spreading your elbows too far apart, bringing the bar to the top of your head. Then return to the initial position.", R.drawable.lying_tricep));
                break;

            case "Abs":
                exercises.add(new Exercise("Crunch", "Lying on the ground on your back with bent knees and feet flat on the ground. You can put your hands behind your head or on your chest.\nContract your abs by raising your shoulders and upper back towards your knees. Keep your lower back firmly on the ground. Stay in the upright position for a second, then slowly return to the original position.", R.drawable.crunch));
                exercises.add(new Exercise("Hanging Leg Raise", "Hanging from a high bar, hands in pronation.\nContract your abs by moving your knees and hips up towards your chest. Stay in the upright position for one second, then slowly return to the starting position.", R.drawable.leg_raise));
                exercises.add(new Exercise("Plank", "Lying face down on the floor. Balance yourself on your forearms and toes, keeping your shoulders and buttocks at the same height.\nHold this position for the desired length of time while contracting your abs.", R.drawable.plank));
                break;

            case "Legs":
                exercises.add(new Exercise("Leg Press", "Sitting on the press bench, feet flat on the platen, shoulder width apart.\nRelease the manual safety catch and slowly lower the load by bringing your knees towards your chest. When your knees are at a 90° angle, pause for a moment and then slowly raise the load. To protect your knees, stop the movement just before your legs are fully extended. During the movement, do not lift your buttocks off the bench.", R.drawable.leg_press));
                exercises.add(new Exercise("Leg Extension", "Adjust the leg extension so that when you sit down your knees are at the edge of the bench and your ankles are just below the footrest. Sit with your back firmly against the backrest, holding the handles on the sides.\nExtend your legs until they are fully extended. Hold the load for a moment by contracting your quadriceps, then return to the lowered position.", R.drawable.leg_ext));
                exercises.add(new Exercise("Leg Curl", "Lie on your stomach on the \"leg curl lying down\" machine with the back of your ankles pressed against the footrest. Grasp the handles.\nLeaning firmly on the bench, bend your legs as far as possible. Hold the load for a moment in the high position by contracting your ischial muscles, then slowly return to the low position.", R.drawable.leg_curl));
                break;

            default:
                break;
        }
        return exercises;
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