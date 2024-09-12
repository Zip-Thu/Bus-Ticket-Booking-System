package com.example.btl2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class User_Fragment extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fragment);

        bottomNavigationView = findViewById(R.id.bottom_navigation_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment_user = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment_user = new User_Ticket();
            } else if (id == R.id.nav_info) {
                selectedFragment_user = new User_Information();
            } else if (id == R.id.nav_user) {
                selectedFragment_user = new User_Account();
            }

            if (selectedFragment_user != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment_user)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}