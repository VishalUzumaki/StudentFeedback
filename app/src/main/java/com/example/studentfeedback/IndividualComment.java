package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private Integer upvoteRank,downvoteRank,strikeRank;

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

        final DatabaseReference commentRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("comments").child(commentId);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String temp_descripton =dataSnapshot.child("text").getValue().toString()+"\n\n";
                temp_descripton=temp_descripton+ "Upvote : "+dataSnapshot.child("upvote").getValue().toString() + "  |  Downvote : "+ dataSnapshot.child("downvote").getValue().toString();

                description.setText(temp_descripton);

                upvoteRank = Integer.parseInt(dataSnapshot.child("upvote").getValue().toString());
                downvoteRank = Integer.parseInt(dataSnapshot.child("downvote").getValue().toString());
                strikeRank = Integer.parseInt(dataSnapshot.child("strike").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvoteRank=upvoteRank+1;
                commentRef.child("upvote").setValue(String.valueOf(upvoteRank) );
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvoteRank=downvoteRank+1;
                commentRef.child("downvote").setValue(String.valueOf(downvoteRank) );
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strikeRank=strikeRank+1;
                commentRef.child("strike").setValue(String.valueOf(strikeRank) );
            }
        });

    }


}