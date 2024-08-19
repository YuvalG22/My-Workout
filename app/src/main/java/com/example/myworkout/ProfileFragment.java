package com.example.myworkout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private MaterialTextView main_LBL_name;
    private MaterialTextView main_LBL_email;
    private MaterialTextView main_LBL_phone;
    private AppCompatImageView main_IMG_profile;
    private FloatingActionButton main_FAB_logout;
    private FloatingActionButton main_FAB_edit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            if (user.getPhotoUrl() != null) {
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl.replace("s96-c", "s600-c");
                Glide.with(this).load(photoUrl).circleCrop().placeholder(R.drawable.blank_profile).into(main_IMG_profile);
                main_LBL_name.setText(user.getDisplayName());
                main_LBL_email.setText(user.getEmail());
                main_LBL_phone.setText(user.getPhoneNumber());
            }
        }
        main_FAB_logout.setOnClickListener(v -> onLogOutClicked());
        main_FAB_edit.setOnClickListener(v -> onEditClicked());
    }

    private void onEditClicked() {
        Intent intent = new Intent(getContext(), ProfileEditActivity.class);
        startActivity(intent);
    }

    private void onLogOutClicked() {
        AuthUI.getInstance()
                .signOut(requireActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        if(getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });
    }

    public void findViews(View v){
        main_IMG_profile = v.findViewById(R.id.main_IMG_profile);
        main_LBL_name = v.findViewById(R.id.main_LBL_name);
        main_LBL_email = v.findViewById(R.id.main_LBL_email);
        main_LBL_phone = v.findViewById(R.id.main_LBL_phone);
        main_FAB_logout = v.findViewById(R.id.main_FAB_logout);
        main_FAB_edit = v.findViewById(R.id.main_FAB_edit);
    }
}