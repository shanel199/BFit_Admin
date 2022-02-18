package com.shcollege.bfitadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class exercises extends AppCompatActivity implements View.OnClickListener {
    private TextView add_exercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        add_exercise = (TextView) findViewById(R.id.add_exercise);
        add_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(exercises.this, add_exercise.class));
            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}