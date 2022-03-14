package com.shcollege.bfitadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView logoutbtn,diet_banner,add_diet;
    private ImageView exercise_iv,profile_iv,diet_iv,workout_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutbtn = (TextView) findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);
        diet_iv =(ImageView) findViewById(R.id.diet_iv);
        //diet_banner =(TextView) findViewById(R.id.diet_banner);
        workout_iv =(ImageView) findViewById(R.id.workout_iv);
        profile_iv =(ImageView) findViewById(R.id.profile_iv);
        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, profile.class));
            }
        });
        workout_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, workout.class));
            }
        });
        exercise_iv =(ImageView) findViewById(R.id.exercise_iv);
        exercise_iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, exercises.class));
            }
        });
        diet_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, diet.class));
            }
        });


    }

    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.logoutbtn:
                    logout();
                    break;

        }


}
    private void logout() {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();

    }



    }