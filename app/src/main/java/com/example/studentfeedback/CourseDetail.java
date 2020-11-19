package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetail extends AppCompatActivity {

    private TextView courseName,description,syllabus;


    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";

    private Button addReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseName = findViewById(R.id.courseName);
        syllabus = findViewById(R.id.syllabus);
        description = findViewById(R.id.description);

        addReview = findViewById(R.id.addReview);



        Intent courseNameIntent = getIntent();

        universityName = courseNameIntent.getStringExtra("universityName");

        String courseTitlearray[] = courseNameIntent.getStringExtra("courseName").split(" ");

        courseTitle = courseTitlearray[0]+" "+courseTitlearray[1];

        departmentSelected = courseNameIntent.getStringExtra("departmentSelected");

        String courseHeader = "";

        for(int i=0;i<courseTitlearray.length;i++)
        {
            courseHeader=courseHeader+courseTitlearray[i]+" ";
        }

        courseName.setText(departmentSelected+" "+courseHeader);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference subjectRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle);

        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("reach","reaching inside the subject" + dataSnapshot);

                if(dataSnapshot.getValue() != null) {
                    description.setText("Course Description: " + dataSnapshot.child("description").getValue().toString());
                    syllabus.setText("Overall Rating: " + dataSnapshot.child("syllabus").getValue().toString());
                }
                else{
                    Toast.makeText(CourseDetail.this,"Incosistent Data present for this course ",Toast.LENGTH_LONG).show();
                }

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