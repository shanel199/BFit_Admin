package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import utilities.Diet;
import utilities.Workout;

public class DietHome extends AppCompatActivity{

    TextView edit_diet,add_diet;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<Diet> options;
    FirebaseFirestore firestore;
    String TITLE,CATEGORY,DESCRIPTION,CALORIE,AUTHOR,DURATION,VIDEO_URI,IMAGE_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_home);

        edit_diet = findViewById(R.id.edit_diet);
        add_diet = findViewById(R.id.diet_add);
        recyclerView = findViewById(R.id.diet_recycler_view);

        firestore = FirebaseFirestore.getInstance();

        edit_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietHome.this,DeleteDiet.class));
            }
        });

        Query query = firestore.collection("DIETS");

        options = new FirestoreRecyclerOptions.Builder<Diet>()
                .setQuery(query,Diet.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Diet,MyViewHolder>(options) {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_workouts,parent,false);
                return new MyViewHolder(v);

            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Diet model) {

                holder.title.setText(model.getTitle());
                holder.calorie.setText(model.getCalorie()+" KCAL");

                if(model.getDiet_image_uri()==""){
                    holder.image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_not_supported_24));
                }
                else{
                    Glide.with(getApplicationContext()).load(model.getDiet_image_uri()).into(holder.image); //dependcenies is added for glide in build.gradle
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TITLE = model.getTitle();
                        CATEGORY = model.getCategory();
                        DESCRIPTION = model.getDescription();
                        CALORIE = model.getCalorie();
                        AUTHOR = model.getAuthor();
                        DURATION = model.getDuartion();
                        IMAGE_URI = model.getDiet_image_uri();
                        VIDEO_URI = model.getDiet_video_uri();

                        Intent i = new Intent(DietHome.this,DietDetails.class);
                        i.putExtra("TITLE",TITLE);
                        i.putExtra("CATEGORY",CATEGORY);
                        i.putExtra("CALORIE",CALORIE);
                        i.putExtra("AUTHOR",AUTHOR);
                        i.putExtra("DURATION",DURATION);
                        i.putExtra("IMAGE URI",IMAGE_URI);
                        i.putExtra("DESCRIPTION",DESCRIPTION);
                        i.putExtra("VIDEO URI",VIDEO_URI);

                        startActivity(i);
                    }
                });


            }
        };

        adapter.startListening();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        add_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietHome.this,AddDiet.class));
            }
        });




    }

    class  MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,calorie;
        ImageView image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            calorie = itemView.findViewById(R.id.textView_workoutCategoryCard);
            title = itemView.findViewById(R.id.textView_workoutNameCard);
            image = itemView.findViewById(R.id.imageView_workoutThumbnailCard);
            cardView = itemView.findViewById(R.id.cardview_workout_card2);

        }
    }

}