package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

public class CourseSurvey extends AppCompatActivity {

    private Button submitReview;

    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";


    private Spinner professorList, standingsList, gradesList;
    private EditText professorRatingValue, courseRatingValue, hoursValue, difficultyValue, commentValue;


    @IgnoreExtraProperties
    public class Comments {

        public String text,downvote,strike,upvote,timestamp,username;

        public Comments() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Comments(String downvote,String strike, String text, String timestamp, String upvote, String username) {
            this.username = username;
            this.downvote = downvote;
            this.upvote = upvote;
            this.strike = strike;
            this.text = text;
            this.timestamp = timestamp;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_survey);



        standingsList = findViewById(R.id.standingsReview);
        professorList = findViewById(R.id.porfessorSelection);
        gradesList = findViewById(R.id.gradeReview);


        professorRatingValue = findViewById(R.id.professorRatingReview);
        courseRatingValue = findViewById(R.id.courseRatingreview);
        hoursValue = findViewById(R.id.hoursReview);
        difficultyValue = findViewById(R.id.difficultyreview);
        commentValue = findViewById(R.id.commentsReview);



        submitReview = findViewById(R.id.submitSurvey);


        Intent getDetials = getIntent();

        universityName=getDetials.getStringExtra("University");
        departmentSelected=getDetials.getStringExtra("Department");
        courseTitle=getDetials.getStringExtra("CourseName");


        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference rootRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle);

//                root reference will refer to the subject from here we have to set to its sub propoerties like
//                reference to rating, syllabus, etc.

                DatabaseReference reviewReference = rootRef.child("comments");

                String temp_comment = commentValue.getText().toString();

                Date date = new Date();

                Comments comment = new Comments("0","0",temp_comment,date.toString(),"0","Anonymous");

                reviewReference.push().setValue(comment);

            }
        });


    }
}