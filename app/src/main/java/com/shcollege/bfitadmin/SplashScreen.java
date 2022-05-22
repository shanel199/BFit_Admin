package com.shcollege.bfitadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    int i=1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("REMEMBER",MODE_PRIVATE);
        String username = pref.getString("USERNAME", null);
        String password = pref.getString("PASSWORD", null);

        if (username == null || password == null) {
            i=0;
        }

        handler = new Handler();
        runnable = () ->
        {

            if(i==0)
            {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                //        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
            else
            {
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                //        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }

        };
            handler.postDelayed(runnable, 3000);

    }
}
