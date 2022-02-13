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


    private TextView logoutbtn;
    private ImageView exercise_iv,equipment_iv,diet_iv,bodypart_iv,workout_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutbtn = (TextView) findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);
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