package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseSurvey extends AppCompatActivity {


    private EditText overall_review;
    private Button submitReview;
    private String UniversityName, CourseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_survey);

        overall_review= findViewById(R.id.review);
        submitReview = findViewById(R.id.submitSurvey);


        Intent getDetials = getIntent();

        UniversityName=getDetials.getStringExtra("University");
        CourseTitle=getDetials.getStringExtra("CourseName");


        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference rootRef = database.getReference().child("University").child(UniversityName).child(CourseTitle);

//                root reference will refer to the subject from here we have to set to its sub propoerties like
//                reference to rating, syllabus, etc.

                DatabaseReference reviewReference = rootRef.child("review");

                String temp_review = overall_review.getText().toString();

                reviewReference.push().setValue(temp_review);

            }
        });


    }
}