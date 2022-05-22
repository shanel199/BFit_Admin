package com.shcollege.bfitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

import kotlinx.coroutines.scheduling.WorkQueueKt;
import utilities.Workout;

public class WorkoutHome extends AppCompatActivity{

    TextView edit_workout,add_workout;
    RecyclerView recyclerView;

    FirestoreRecyclerOptions<Workout> options;
    FirestoreRecyclerAdapter adapter;
    FirebaseFirestore firestore;
    String TITLE,CATEGORY,DESCRIPTION,IMAGE_URI,VIDEO_URI,INSTRUCTOR_NAME,DIFFICULTY,CALORIES_BURNT,DURATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_home);

        edit_workout = findViewById(R.id.edit_workout);
        add_workout = findViewById(R.id.workout_add);
        recyclerView = findViewById(R.id.workout_recycler_view);

        firestore = FirebaseFirestore.getInstance();

        add_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutHome.this,AddWorkout.class));
            }
        });

        edit_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutHome.this,DeleteWorkout.class));
            }
        });

        Query query = firestore.collection("WORKOUTS");

        options = new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(query, Workout.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Workout,MyViewHolder>(options) {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_workouts,parent,false);
                return new MyViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Workout model) {


                holder.title.setText(model.getTitle());
//                holder.reps.setText("REPS : "+model.getReps());
//                holder.sets.setText("SETS : "+model.getSets());
//                holder.goal.setText("GOAL : "+model.getGoal());
                holder.category.setText(model.getCategory());

                if(model.getImage_uri()=="" || model.getImage_uri()==null){
                    holder.image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_not_supported_24));
                }
                else{
                    Glide.with(getApplicationContext()).load(model.getImage_uri()).into(holder.image); //dependencies are added for glide in build.gradle
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TITLE = model.getTitle();
                        CATEGORY = model.getCategory();
                        DESCRIPTION = model.getDescription();
                        INSTRUCTOR_NAME = model.getInstructor_name();
                        DIFFICULTY = model.getDifficulty();
                        CALORIES_BURNT = model.getCalories_burnt();
                        DURATION = model.getDuration();
                        IMAGE_URI = model.getImage_uri();
                        VIDEO_URI = model.getVideo_uri();

                        Intent i = new Intent(WorkoutHome.this,WorkoutDetails.class);
                        i.putExtra("TITLE",TITLE);
                        i.putExtra("CATEGORY",CATEGORY);
                        i.putExtra("IMAGE URI",IMAGE_URI);
                        i.putExtra("INSTRUCTOR NAME",INSTRUCTOR_NAME);
                        i.putExtra("DIFFICULTY",DIFFICULTY);
                        i.putExtra("CALORIES BURNT",CALORIES_BURNT);
                        i.putExtra("DURATION",DURATION);
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


    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,category,sets,reps,description,goal,time;
        ImageView image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.textView_workoutCategoryCard);
           // description = itemView.findViewById(R.id.tv_description2);
//            reps = itemView.findViewById(R.id.tv_reps);
//            sets = itemView.findViewById(R.id.tv_sets);
//            goal = itemView.findViewById(R.id.tv_goal);
            title = itemView.findViewById(R.id.textView_workoutNameCard);
            image = itemView.findViewById(R.id.imageView_workoutThumbnailCard);
            cardView = itemView.findViewById(R.id.cardview_workout_card2);


        }
    }

}