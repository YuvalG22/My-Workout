package com.example.myworkout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.myworkout.Models.Workout;
import com.example.myworkout.Utilities.CustomLatLng;
import com.example.myworkout.Utilities.DateFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.example.myworkout.Utilities.TimeFormatter;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.List;

public class WorkoutSummary extends ToolbarActivity implements OnMapReadyCallback {

    private MaterialTextView main_LBL_duration;
    private MaterialTextView main_LBL_distance;
    private MaterialTextView main_LBL_pace;
    private MaterialTextView main_LBL_calories;
    private MaterialCardView card_distance;
    private MaterialCardView card_pace;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        workout = getIntent().getParcelableExtra("WORKOUT");

        setupToolbar(R.id.toolbar, true);
        getSupportActionBar().setTitle(workout.getType());
        getSupportActionBar().setSubtitle(DateFormatter.formatToDayMonthTime(workout.getStartTime()));

        if ("Gym".equalsIgnoreCase(workout.getType())) {
            findViewById(R.id.summary_map_fragment).setVisibility(View.GONE);
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.summary_map_fragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        workout = getIntent().getParcelableExtra("WORKOUT");
        findViews();
        initViews();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        displayWorkoutPath();
    }

    private void initViews() {
        main_LBL_duration.setText(TimeFormatter.formatTime(workout.getDuration()));
        int roundedCalories = (int) Math.round(workout.getCaloriesBurnt());
        main_LBL_calories.setText(String.format("%d kcal", roundedCalories));
        main_LBL_distance.setText(String.format("%.2f km", workout.getDistance() / 1000));
        main_LBL_pace.setText(String.format("%.2f min/km", workout.getPace() / 60));
        if(workout.getType().equals("Gym")) {
            card_distance.setVisibility(View.GONE);
            card_pace.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        main_LBL_duration = findViewById(R.id.main_LBL_duration);
        main_LBL_distance = findViewById(R.id.main_LBL_distance);
        main_LBL_pace = findViewById(R.id.main_LBL_pace);
        main_LBL_calories = findViewById(R.id.main_LBL_calories);
        card_distance = findViewById(R.id.card_distance);
        card_pace = findViewById(R.id.card_pace);
    }

    private void displayWorkoutPath() {
        if (mMap != null && workout != null) {
            List<CustomLatLng> customPathPoints = workout.getPathPoints();
            List<LatLng> pathPoints = convertToLatLng(customPathPoints);
            if (pathPoints != null && !pathPoints.isEmpty()) {
                PolylineOptions polylineOptions = new PolylineOptions().addAll(pathPoints).color(Color.BLUE).width(12);
                mMap.addPolyline(polylineOptions);
                mMap.addMarker(new MarkerOptions().position(pathPoints.get(0)).title("Start"));
                mMap.addMarker(new MarkerOptions().position(pathPoints.get(pathPoints.size() - 1)).title("Finish").icon(getMarkerIconFromDrawable(R.drawable.checkeredflag)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.get(0), 17));
            }
        }
    }

    private List<LatLng> convertToLatLng(List<CustomLatLng> customLatLngs) {
        List<LatLng> latLngs = new ArrayList<>();
        for (CustomLatLng customLatLng : customLatLngs) {
            latLngs.add(customLatLng.toLatLng());
        }
        return latLngs;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button action
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BitmapDescriptor getMarkerIconFromDrawable(int resId) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 130, 130, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }
}