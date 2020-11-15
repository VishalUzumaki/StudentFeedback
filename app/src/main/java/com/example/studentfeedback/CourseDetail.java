package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetail extends AppCompatActivity {

    private TextView courseName,difficult,avg_rating;


    private String universityName="";
    private String courseTitle="";

    private Button addReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseName = findViewById(R.id.courseName);
        difficult = findViewById(R.id.difficulty);
        avg_rating = findViewById(R.id.overall_rating);
        addReview = findViewById(R.id.addReview);



        Intent courseNameIntent = getIntent();

        universityName = courseNameIntent.getStringExtra("universityName");
        courseTitle = courseNameIntent.getStringExtra("courseName");

        courseName.setText(universityName+ " "+courseTitle);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference subjectRef = database.getReference().child("University").child(universityName).child(courseTitle);

        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("reach","reaching inside the subject" + dataSnapshot);
                difficult.setText("Difficulty level: "+dataSnapshot.child("difficulty").getValue().toString());
                avg_rating.setText("Overall Rating: "+dataSnapshot.child("average_rating").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("failed", "Failed to read value.", error.toException());
            }
        });


        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addSurvey = new Intent(CourseDetail.this,CourseSurvey.class);

                addSurvey.putExtra("University",universityName);
                addSurvey.putExtra("CourseName",courseTitle);

                startActivity(addSurvey);


            }
        });

    }
}