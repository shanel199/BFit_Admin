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

    private EditText firstName, lastName, emailRegister, passwordRegister, editText_firstName, editText_lastName, editText_email, editText_password;
    private TextView login, register, textView_login, textView_register;
    private ImageView googleIcon, facebookIcon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.textView_register);
        register.setOnClickListener(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        firstName = (EditText) findViewById(R.id.editText_firstName);
        lastName = (EditText) findViewById(R.id.editText_lastName);
        emailRegister = (EditText) findViewById(R.id.email_et);
        passwordRegister = (EditText) findViewById(R.id.editText_password);
        login = (TextView) findViewById(R.id.textView_login);
        register = (TextView) findViewById(R.id.textView_register);
        googleIcon = (ImageView) findViewById(R.id.imageView_google);
        facebookIcon = (ImageView) findViewById(R.id.imageView_facebook);
        register.setOnClickListener(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.textView_register:
                        register();
                        break;
                }
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void register() {
    }

    @Override
    public void onClick(View v) {
    }

    public void sign_up() {

        String firstName = editText_firstName.getText().toString().trim();
        String lastName = editText_lastName.getText().toString().trim();
        String emailRegister = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String id = mAuth.getCurrentUser().getUid();
        String imageurl = "default";

        if (firstName.isEmpty()) {
            editText_firstName.setError("This field is required");
            editText_firstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            editText_lastName.setError("This field is required");
            editText_lastName.requestFocus();
            return;
        }
        if (emailRegister.isEmpty()) {
            editText_email.setError("This field is required");
            editText_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailRegister).matches()) {
            editText_email.setError("Please provide a valid email");
            editText_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editText_password.setError("This field is required");
            editText_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editText_password.setError("Min password length should be 6 characters");
            editText_password.requestFocus();
            return;
        }
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(emailRegister, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(firstName, lastName, emailRegister, password);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to sign up,Try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Failed to sign up,Try again!", Toast.LENGTH_SHORT).show();
                }
                }});


                    }
                }
