package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import utilities.User;

public class ProfileActivity extends AppCompatActivity {

    ImageView edit_name;
    Button reset_password,save;
    TextView display_name,display_email;
    TextInputLayout name;
    EditText et_name;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit_name = findViewById(R.id.image_edit_name);
        reset_password = findViewById(R.id.reset_password);
        display_email = findViewById(R.id.profile_display_email);
        display_name = findViewById(R.id.profile_name_display);
        name = findViewById(R.id.textInputLayout_name);
        et_name = findViewById(R.id.et_name_profile);
        save = findViewById(R.id.btn_save_name);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Admin Credentials");

        reference.child(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DataSnapshot snapshot = task.getResult();
                            User obj = snapshot.getValue(User.class);

                            display_name.setText(obj.getFirstname());
                            display_email.setText(obj.getEmail());

                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_name = et_name.getText().toString();

                if(s_name.isEmpty())
                {
                    et_name.setError("Enter a valid name");
                    et_name.requestFocus();
                    return;
                }

                Map<String,Object> data = new HashMap<>();
                data.put("firstname",s_name);

                reference.child(auth.getCurrentUser().getUid())
                        .updateChildren(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ProfileActivity.this,"Name updated successfully", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                save.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);

            }
        });

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ResetPassword.class));
            }
        });




    }
}