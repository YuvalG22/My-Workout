package com.example.myworkout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    private BottomNavigationView main_NAV_bar;
    private FrameLayout main_FRG_frame;
    private HomeFragment homeFragment;
    private WorkoutsFragment workoutsFragment;
    private LogFragment logFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        fixBottomNavBar();
        homeFragment = new HomeFragment();
        workoutsFragment = new WorkoutsFragment();
        logFragment = new LogFragment();
        profileFragment = new ProfileFragment();
        main_NAV_bar.setOnItemSelectedListener(this);
        main_NAV_bar.setSelectedItemId(R.id.menu_home);
    }

    private void findViews() {
        main_NAV_bar = findViewById(R.id.main_NAV_bar);
        main_FRG_frame = findViewById(R.id.main_FRG_frame);
    }

    private void fixBottomNavBar() {
        ViewCompat.setOnApplyWindowInsetsListener(main_NAV_bar, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, bottomInset);
            return insets;
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_FRG_frame, homeFragment)
                    .commit();
            return true;
        }
        else if (itemId == R.id.menu_workouts) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_FRG_frame, workoutsFragment)
                    .commit();
            return true;
        }
        else if (itemId == R.id.menu_log) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_FRG_frame, logFragment)
                    .commit();
            return true;
        }
        else if (itemId == R.id.menu_profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_FRG_frame, profileFragment)
                    .commit();
            return true;
        }
        return false;
    }
}