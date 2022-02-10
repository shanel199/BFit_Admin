package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailLogin, passwordLogin,email_et,editText_password;
    private TextView forgotPassword, login, register,textView_forgotPassword,textView_login,textView_register;
    private ImageView googleIcon, facebookIcon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.textView_register);
        register.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        emailLogin = (EditText) findViewById(R.id.email_et);
        passwordLogin = (EditText) findViewById(R.id.editText_password);
        forgotPassword = (TextView) findViewById(R.id.textView_forgotPassword);
        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login = (TextView) findViewById(R.id.textView_login);
        register = (TextView) findViewById(R.id.textView_register);
        googleIcon = (ImageView) findViewById(R.id.imageView_google);
        facebookIcon = (ImageView) findViewById(R.id.imageView_facebook);
        register.setOnClickListener(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_register:
                break;
        }
    }

    private void userSignin() {
        String emailLogin = email_et.getText().toString().trim();
        String passwordLogin = editText_password.getText().toString().trim();

        if(emailLogin.isEmpty()){
            email_et.setError("This field is required");
            email_et.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches()){
            email_et.setError("Please provide a valid email");
            email_et.requestFocus();
            return;
        }
        if(passwordLogin.isEmpty()){
            editText_password.setError("This field is required");
            editText_password.requestFocus();
            return;
        }
        if(passwordLogin.length() < 6){
            editText_password.setError("Min password length should be 6 characters");
            editText_password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            {
                                Toast.makeText(MainActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                            }



                    }else {
                            Toast.makeText(MainActivity.this, "Failed to sign up,Try again!", Toast.LENGTH_SHORT).show();
                    }
                }
    });
}
                    }