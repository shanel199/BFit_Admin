package com.shcollege.bfitadmin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utilities.Workout;

public class AddWorkout extends AppCompatActivity {

    EditText title,description,instructor_name,duration,calories_burnt, category;
    Spinner spinner_difficulty;
    Button add_workout;
    String difficulty;
    FirebaseFirestore firestore;
    ArrayList<String>arrayList;
    ArrayAdapter<String>arrayAdapter;
    ImageView add_image,add_video;
    FirebaseStorage firebaseStorage;
    StorageReference reference;
    Uri image_uri,video_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        title = findViewById(R.id.et_title);
        category = findViewById(R.id.et_category);
        description = findViewById(R.id.et_description);
        instructor_name = findViewById(R.id.et_instructor_name);
        duration = findViewById(R.id.et_duration);
        calories_burnt = findViewById(R.id.et_calories_burnt);
        add_workout = findViewById(R.id.btn_add_workout);
        spinner_difficulty = findViewById(R.id.spinner_difficulty);
        add_image = findViewById(R.id.add_image_workout);
        add_video = findViewById(R.id.add_video_workout);

        arrayList = new ArrayList<String>();
        arrayList.add("Easy");
        arrayList.add("Intermediate");
        arrayList.add("Hard");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner_difficulty.setAdapter(arrayAdapter);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReference("WORKOUTS");

        add_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(AddWorkout.this);
                progressDialog.setMessage("Adding new workout...\nThis may take some time depending upon the network speed...");
                progressDialog.show();

                String s_title = title.getText().toString();
                String s_category = category.getText().toString();
                String s_description = description.getText().toString();
                String s_instructor_name = instructor_name.getText().toString();
                String s_duration = duration.getText().toString();
                String s_calories_burnt = calories_burnt.getText().toString();
                difficulty = spinner_difficulty.getSelectedItem().toString();
                


                reference.child(s_title).putFile(image_uri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    reference.child(s_title).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Workout obj = new Workout(s_title, s_category, s_description, s_instructor_name, s_duration, s_calories_burnt, difficulty,uri.toString(),"");

                                                    firestore.collection("WORKOUTS").document(s_title)
                                                            .set(obj)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {

                                                                        reference.child(s_title+"-vid").putFile(video_uri)
                                                                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                        if (task.isSuccessful())
                                                                                        {
                                                                                            reference.child(s_title+"-vid").getDownloadUrl()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Uri uri) {
                                                                                                            Map<String,Object>data = new HashMap<>();
                                                                                                            data.put("video_uri",uri.toString());

                                                                                                            firestore.collection("WORKOUTS").document(s_title)
                                                                                                                    .update(data)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if(task.isSuccessful()) {
                                                                                                                                progressDialog.dismiss();
                                                                                                                                Toast.makeText(AddWorkout.this, "Workout added successfully", Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                            else
                                                                                                                                Toast.makeText(AddWorkout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    })
                                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                                        @Override
                                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                                            Toast.makeText(AddWorkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    })
                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            Toast.makeText(AddWorkout.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            Toast.makeText(AddWorkout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(AddWorkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                    }
                                                                    else
                                                                        Toast.makeText(AddWorkout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AddWorkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddWorkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else
                                {
                                    Toast.makeText(AddWorkout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddWorkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        
            }
        });
        
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");
            }
        });

        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher2.launch("video/*");
            }
        });



    }

    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result!=null)
            {
                add_image.setImageURI(result);
                image_uri = result;
                // upload.setVisibility(View.VISIBLE);
            }
            else
            {
                //upload.setVisibility(View.INVISIBLE);
                Toast.makeText(AddWorkout.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }
    });

    ActivityResultLauncher<String> activityResultLauncher2 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result!=null)
            {
                add_video.setImageResource(R.drawable.ic_baseline_check_circle_24);
                video_uri = result;
                // upload.setVisibility(View.VISIBLE);
            }
            else
            {
                //upload.setVisibility(View.INVISIBLE);
                Toast.makeText(AddWorkout.this, "No video selected", Toast.LENGTH_SHORT).show();
            }
        }
    });
    
}