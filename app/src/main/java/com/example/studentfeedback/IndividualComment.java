package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IndividualComment extends AppCompatActivity {


    private Button upvote,downvote,report;
    private TextView description;

    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";
    private String commentId;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_comment);

        description = findViewById(R.id.description);
        upvote = findViewById(R.id.upvote);
        downvote = findViewById(R.id.downvote);
        report = findViewById(R.id.report);


        Intent commentDetails = getIntent();

        universityName=commentDetails.getStringExtra("UniversityName");
        departmentSelected=commentDetails.getStringExtra("Department");
        courseTitle=commentDetails.getStringExtra("CouseSelected");
        commentId=commentDetails.getStringExtra("Comment");

        database = FirebaseDatabase.getInstance();

        DatabaseReference commentRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("comments").child(commentId);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                description.setText(dataSnapshot.child("text").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}