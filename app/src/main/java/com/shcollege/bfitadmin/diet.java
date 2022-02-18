package com.shcollege.bfitadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class diet extends AppCompatActivity implements View.OnClickListener {
    private TextView add_diet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        add_diet = (TextView) findViewById(R.id.add_diet);
        add_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(diet.this, add_diet.class));
        }
        });
    }

    @Override
    public void onClick(View v) {

    }
}