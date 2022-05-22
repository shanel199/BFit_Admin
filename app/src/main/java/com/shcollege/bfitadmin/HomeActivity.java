package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utilities.User;

public class HomeActivity extends AppCompatActivity{

    CardView workout,diet,profile,logout;
    TextView username;
    private FirebaseUser fUser;
    private String profileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        workout = findViewById(R.id.home_workout);
        diet = findViewById(R.id.home_diet);
        profile = findViewById(R.id.home_profile);
        logout = findViewById(R.id.home_logout);

        username = findViewById(R.id.dashboard_uname);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        profileID = fUser.getUid();

        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, WorkoutHome.class));
            }
        });

        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DietHome.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        helloUser();
    }

    private void helloUser() {
        FirebaseDatabase.getInstance().getReference().child("Admin Credentials").child(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getFirstname() + " " + user.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void logout() {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences pref = getSharedPreferences("REMEMBER",MODE_PRIVATE);

            pref.edit()
                    .putString("USERNAME", null)
                    .putString("PASSWORD", null)
                    .commit();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
    }

}