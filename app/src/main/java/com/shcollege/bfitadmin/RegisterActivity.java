package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fnameregister, lnameregister, emailregister, passwordregister;
    private TextView alreadylogin, registerbtn;
    private ImageView googleIcon, facebookIcon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        fnameregister = (EditText) findViewById(R.id.fnameregister);
        lnameregister = (EditText) findViewById(R.id.lnameregister);
        emailregister = (EditText) findViewById(R.id.emailregister);
        passwordregister = (EditText) findViewById(R.id.passwordregister);
        alreadylogin = (TextView) findViewById(R.id.alreadylogin);
        alreadylogin.setOnClickListener(this);
        registerbtn = (TextView) findViewById(R.id.registerbtn);
        googleIcon = (ImageView) findViewById(R.id.imageView_google);
        facebookIcon = (ImageView) findViewById(R.id.imageView_facebook);
        registerbtn.setOnClickListener(this);
    }
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.registerbtn:
                        sign_up();
                        break;
                    case R.id.alreadylogin:
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                break;
            }
    }

    private void sign_up() {
        String firstname = fnameregister.getText().toString().trim();
        String lastname = lnameregister.getText().toString().trim();
        String email = emailregister.getText().toString().trim();
        String password = passwordregister.getText().toString().trim();

        if (firstname.isEmpty()){
            fnameregister.setError("This field is required");
            fnameregister.requestFocus();
            return;
        }
        if (lastname.isEmpty()){
            lnameregister.setError("This field is required");
            lnameregister.requestFocus();
            return;
        }
        if (email.isEmpty()){
            emailregister.setError("This field is required");
            emailregister.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordregister.setError("This field is required");
            passwordregister.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailregister.setError("Please provide a valid email");
            emailregister.requestFocus();
            return;
        }
        if(password.length() < 6){
            passwordregister.setError("Min password length should be 6 characters");
            passwordregister.requestFocus();
            return;
        }
        {
            mAuth.fetchSignInMethodsForEmail(emailregister.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean check = !task.getResult().getSignInMethods().isEmpty();
                            if(check)
                            {
                                Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(firstname, lastname, email, password);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user.isEmailVerified()) {
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                                else {
                                    user.sendEmailVerification();
                                    Toast.makeText(RegisterActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                                }
                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });
                }
            }
        });
    }}
