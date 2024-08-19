package com.example.myworkout;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myworkout.Utilities.ToastVibrate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEditActivity extends AppCompatActivity {
    private TextInputEditText main_input_height;
    private TextInputEditText main_input_weight;
    private MaterialButton main_BTN_save;
    private ToastVibrate toastVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        toastVibrate = new ToastVibrate(this);

        findViews();
        loadProfileData();
        initViews();
    }

    private void initViews() {
        main_BTN_save.setOnClickListener(v -> saveProfileData());
    }

    private void loadProfileData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String height = snapshot.child("height").getValue(String.class);
                    String weight = snapshot.child("weight").getValue(String.class);

                    if (height != null) {
                        main_input_height.setText(height);
                    }
                    if (weight != null) {
                        main_input_weight.setText(weight);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void saveProfileData() {
        String height = main_input_height.getText().toString();
        String weight = main_input_weight.getText().toString();
        if (height.isEmpty() || weight.isEmpty()) {
            toastVibrate.toast("Please enter all fields");
            return;
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile");
            userRef.child("height").setValue(height);
            userRef.child("weight").setValue(weight);
            toastVibrate.toast("Profile Updated");
        }
    }

    private void findViews() {
        main_input_height = findViewById(R.id.profile_input_height);
        main_input_weight = findViewById(R.id.profile_input_weight);
        main_BTN_save = findViewById(R.id.profile_btn_save);
    }
}