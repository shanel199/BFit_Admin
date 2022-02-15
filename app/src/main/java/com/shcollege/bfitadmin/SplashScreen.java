package com.shcollege.bfitadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new Handler().postDelayed(new Runnable() {
          //  @Override
          //  public void run() {
          //      Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
          //      SplashScreen.this.startActivity(mainIntent);
           //     SplashScreen.this.finish();

       // },4000);

    handler = new Handler();
    runnable = () ->
    {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    };
        handler.postDelayed(runnable, 3000);

}
}
