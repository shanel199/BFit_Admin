package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emaillogin, passwordlogin;
    private TextView forgotpassword, loginbtn, dontregister;
    private FirebaseAuth mAuth;
    CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_login);

            dontregister = (TextView) findViewById(R.id.dontregister);
            dontregister.setOnClickListener(this);
            mAuth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            emaillogin = (EditText) findViewById(R.id.emaillogin);
            passwordlogin = (EditText) findViewById(R.id.passwordlogin);
            forgotpassword = (TextView) findViewById(R.id.forgotpassword);
            remember_me = findViewById(R.id.remember_me);

            forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            loginbtn = (TextView) findViewById(R.id.loginbtn);
            dontregister = (TextView) findViewById(R.id.dontregister);
            dontregister.setOnClickListener(this);
            loginbtn.setOnClickListener(this);



            forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,ResetPassword.class);
                    startActivity(i);
                }
            });



        } catch (Exception e) {
            Log.e("333", "onCreate: Error ONcREATE", e);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbtn:
                userSignin();
                break;
            case R.id.dontregister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void userSignin() {
        String email = emaillogin.getText().toString().trim();
        String password = passwordlogin.getText().toString().trim();

        if(email.isEmpty()){
            emaillogin.setError("This field is required");
            emaillogin.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaillogin.setError("Please provide a valid email");
            emaillogin.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordlogin.setError("This field is required");
            passwordlogin.requestFocus();
            return;
        }
        if(password.length() < 6){
            passwordlogin.setError("Min password length should be 6 characters");
            passwordlogin.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser Admin = FirebaseAuth.getInstance().getCurrentUser();

                            if (Admin.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Start grinding now!", Toast.LENGTH_SHORT).show();

                                if(remember_me.isChecked())
                                {
                                    SharedPreferences pref = getSharedPreferences("REMEMBER",MODE_PRIVATE);

                                    pref.edit()
                                            .putString("USERNAME", email)
                                            .putString("PASSWORD", password)
                                            .commit();

                                }

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Failed to sign in! Please check your credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}