package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IndividualComment extends AppCompatActivity {


    private Button upvote,downvote,report, back;
    private TextView description, tups, tdowns;

    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";
    private String commentId;

    private FirebaseDatabase database;
    private FirebaseAuth authObj;

    private Integer upvoteRank,downvoteRank,strikeRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_comment);


        authObj = FirebaseAuth.getInstance();
        description = findViewById(R.id.description);
        upvote = findViewById(R.id.upvote);
        downvote = findViewById(R.id.downvote);
        report = findViewById(R.id.report);
        tups = findViewById(R.id.text_up);
        tdowns = findViewById(R.id.text_down);
        back = findViewById(R.id.extended_fab);

        Intent commentDetails = getIntent();

        universityName=commentDetails.getStringExtra("UniversityName");
        departmentSelected=commentDetails.getStringExtra("Department");
        courseTitle=commentDetails.getStringExtra("CouseSelected");
        commentId=commentDetails.getStringExtra("Comment");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), AllComments.class);
                in.putExtra("University", universityName);
                in.putExtra("Department", departmentSelected);
                in.putExtra("CourseName", courseTitle);
                startActivity(in);
                finish();
            }
        });

        database = FirebaseDatabase.getInstance();

        final DatabaseReference commentRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("comments").child(commentId);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String temp_descripton =dataSnapshot.child("text").getValue().toString();
                String u = "Upvotes: " + dataSnapshot.child("upvote").getValue().toString();
                String d = "Downvotes: " + dataSnapshot.child("downvote").getValue().toString();
                tups.setText(u);
                tdowns.setText(d);
//                temp_descripton=temp_descripton+ "Upvote : "+dataSnapshot.child("upvote").getValue().toString() + "  |  Downvote : "+ dataSnapshot.child("downvote").getValue().toString();

                description.setText(temp_descripton);

                upvoteRank = Integer.parseInt(dataSnapshot.child("upvote").getValue().toString());
                downvoteRank = Integer.parseInt(dataSnapshot.child("downvote").getValue().toString());
                strikeRank = Integer.parseInt(dataSnapshot.child("strike").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        Providing three action buttons either to upvote downvote or report any comments.
//        comments with more then 3 strikes will no longer be displayed.
//        currently allwouing multiple reports from the same person.

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



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

//        checking if the user has logged out
        if(currentUser == null){

            Intent openDashboard = new Intent(IndividualComment.this, SelectUniversity.class);
            startActivity(openDashboard);
            finish();
        }

    }

}