package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseSurvey extends AppCompatActivity {

    private Button submitReview;

    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";


    private Spinner professorList, standingsList, gradesList;
    private EditText professorRatingValue, courseRatingValue, hoursValue, difficultyValue, commentValue;

    private List<String> standingsOptions,gradesOptions;


    private String finalStandings,finalGrade,finalProfessor;

    private String tempStandings="",tempTotalStandings="";


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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_survey);



        standingsList = findViewById(R.id.standingsReview);

        standingsOptions =  new ArrayList<String>();
        standingsOptions.add("");
        standingsOptions.add("freshman");
        standingsOptions.add("junior");
        standingsOptions.add("masters");
        standingsOptions.add("phd");
        standingsOptions.add("senior");
        standingsOptions.add("sophomor");



        professorList = findViewById(R.id.porfessorSelection);

        gradesList = findViewById(R.id.gradeReview);

        gradesOptions = new ArrayList<String>();
        gradesOptions.add("");
        gradesOptions.add("A");
        gradesOptions.add("A-");
        gradesOptions.add("B");
        gradesOptions.add("B-");
        gradesOptions.add("C");
        gradesOptions.add("C-");
        gradesOptions.add("other");


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





        final ArrayAdapter<String> standingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, standingsOptions);


        standingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        standingsList.setAdapter(standingsAdapter);

        standingsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                        if(position!=0){
                                                            finalStandings = standingsOptions.get(position);
                                                        }
                                                       else {
//                                                        Toast.makeText(CourseSurvey.this, standingsOptions.get(position), Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference rootRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle);

//                root reference will refer to the subject from here we have to set to its sub propoerties like
//                reference to rating, syllabus, etc.


//              adding  comment
                DatabaseReference reviewReference = rootRef.child("comments");

                final String temp_comment = commentValue.getText().toString();

                Date date = new Date();

                Comments comment = new Comments("0","0",temp_comment,date.toString(),"0","Anonymous");

//                reviewReference.push().setValue(comment);


//              updating standings

                if(finalStandings.length()>0) {

                    final DatabaseReference standingReference = rootRef.child("Standing").child(finalStandings);


                     standingReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Integer temporaryStandings;

                            temporaryStandings = Integer.parseInt(dataSnapshot.getValue().toString());

                            temporaryStandings = temporaryStandings+1;

                            tempStandings = String.valueOf(temporaryStandings);
                            Log.d("tempstandings",tempStandings + dataSnapshot.getValue());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

//                    Log.d("tempstandings",tempStandings);

                    if(tempStandings.length()>0) {
                        standingReference.setValue(tempStandings);
                    }


                    Log.d("endingNaruto", tempStandings);


                    final DatabaseReference standingReferenceTotal = rootRef.child("Standing").child("total");

                    standingReferenceTotal.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Integer temporaryStandings=0;

                            temporaryStandings = Integer.parseInt(dataSnapshot.getValue().toString());

                            temporaryStandings = temporaryStandings+1;

                            tempTotalStandings = String.valueOf(temporaryStandings);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });



                    if(tempTotalStandings.length()>0) {
                        standingReferenceTotal.setValue(tempTotalStandings);
                    }




                }

            }
        });


    }
}