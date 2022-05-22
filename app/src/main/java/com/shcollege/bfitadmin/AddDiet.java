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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import utilities.Diet;

public class AddDiet extends AppCompatActivity {

    EditText title,category,description,calorie,author,duration;
    ImageView add_image,add_video;
    Button add_diet;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri image_uri,video_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diet);

        title = findViewById(R.id.et_title_diet);
        category = findViewById(R.id.et_category_diet);
        description = findViewById(R.id.et_description_diet);
        calorie = findViewById(R.id.et_calorie_diet);
        author = findViewById(R.id.et_author_diet);
        duration = findViewById(R.id.et_duartion_diet);
        add_image = findViewById(R.id.diet_image_picker);
        add_diet = findViewById(R.id.btn_add_diet);
        add_video = findViewById(R.id.diet_video_picker);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("DIETS");

        add_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(AddDiet.this);
                progressDialog.setMessage("Adding new diet...\nThis may take some time depending upon the network speed...");
                progressDialog.show();

                String s_title = title.getText().toString();
                String s_category = category.getText().toString();
                String s_description = description.getText().toString();
                String s_calorie = calorie.getText().toString();
                String s_author = author.getText().toString();
                String s_duration = duration.getText().toString();

                storageReference.child(s_title).putFile(image_uri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    storageReference.child(s_title).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Diet obj = new Diet(s_title,s_category,s_description,s_calorie,s_author,s_duration,uri.toString(),"");

                                                    firestore.collection("DIETS").document(s_title)
                                                            .set(obj)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {

                                                                        storageReference.child(s_title+"-vid").putFile(video_uri)
                                                                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                        if (task.isSuccessful())
                                                                                        {
                                                                                            storageReference.child(s_title+"-vid").getDownloadUrl()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Uri uri) {
                                                                                                            Map<String,Object> data = new HashMap<>();
                                                                                                            data.put("diet_video_uri",uri.toString());

                                                                                                            firestore.collection("DIETS").document(s_title)
                                                                                                                    .update(data)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if(task.isSuccessful()) {
                                                                                                                                progressDialog.dismiss();
                                                                                                                                Toast.makeText(AddDiet.this, "Diet added successfully", Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                            else
                                                                                                                                Toast.makeText(AddDiet.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    })
                                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                                        @Override
                                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                                            Toast.makeText(AddDiet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    })
                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            Toast.makeText(AddDiet.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            Toast.makeText(AddDiet.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(AddDiet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                    }
                                                                    else
                                                                        Toast.makeText(AddDiet.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AddDiet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddDiet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(AddDiet.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddDiet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddDiet.this, "No image selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddDiet.this, "No video selected", Toast.LENGTH_SHORT).show();
            }
        }
    });

}