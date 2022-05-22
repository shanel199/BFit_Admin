package com.shcollege.bfitadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import utilities.VPAdapter;

public class WorkoutDetails extends AppCompatActivity {

    TextView title,author;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ImageView imageView;
    String[] headings = new String[]{"DESCRIPTION","VIDEO"};
    VPAdapter adapter;
    String s_title,s_category,s_desc,s_instructor_name,s_difficulty,s_calories_burnt,s_duration,s_image_uri,s_video_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        title = findViewById(R.id.workout_details_title);
        author = findViewById(R.id.workout_details_author);
        imageView = findViewById(R.id.workout_details_image);
        tabLayout = findViewById(R.id.workout_details_tablayout);
        viewPager = findViewById(R.id.workout_details_view_pager);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            s_title = bundle.getString("TITLE");
            s_category = bundle.getString("CATEGORY");
            s_desc = bundle.getString("DESCRIPTION");
            s_instructor_name = bundle.getString("INSTRUCTOR NAME");
            s_difficulty = bundle.getString("DIFFICULTY");
            s_calories_burnt = bundle.getString("CALORIES BURNT");
            s_duration = bundle.getString("DURATION");
            s_image_uri = bundle.getString("IMAGE URI");
            s_video_uri = bundle.getString("VIDEO URI");
        }

        title.setText(s_title);
        author.setText(s_instructor_name);

        if(s_image_uri==""){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_not_supported_24));
        }
        else{
            Glide.with(getApplicationContext()).load(s_image_uri).into(imageView); //dependencies are added for glide in build.gradle
        }

        adapter = new VPAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager,(((tab, position) -> tab.setText(headings[position])))).attach();


    }

    public String getS_title() {
        return s_title;
    }

    public String getS_category() {
        return s_category;
    }

    public String getS_desc() {
        return s_desc;
    }

    public String getS_video_uri() {
        return s_video_uri;
    }

    public String getS_instructor_name() {
        return s_instructor_name;
    }

    public String getS_difficulty() {
        return s_difficulty;
    }

    public String getS_calories_burnt() {
        return s_calories_burnt;
    }

    public String getS_duration() {
        return s_duration;
    }
}