package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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



    private Spinner professorList, standingsList, gradesList , professorRatingValue, courseRatingValue, difficultyValue;
    private EditText  commentValue,hoursValue;

    private List<String> standingsOptions;
    private List<String> gradesOptions;
    private List<String> professorOptions;
    private List<String> scalingRate;


    private String finalStandings="",finalGrade="",finalProfessor="", finalProfessorRating="", finalCourseRating="", finalCourseDifficulty="";

    private String tempStandings="",tempTotalStandings="";
    private Integer tempCourseRatings=0,getTempCourseRatingsTotal=0;
    private Integer tempProfessorRatings=0,getProfessorRatingsTotal=0;
    private Integer tempCourseDifficulty=0,getCourseDifficulty=0;
    private Integer temp_hr_weeks=0, getTemp_hr_weeks=0;




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

        professorOptions = new ArrayList<String>();

        professorOptions.add(" ");




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


        scalingRate = new ArrayList<String>();
        scalingRate.add(" ");

        for(int i=1;i<6;i++)
        {
            scalingRate.add(String.valueOf(i));
        }



        commentValue = findViewById(R.id.commentsReview);



        submitReview = findViewById(R.id.submitSurvey);


        Intent getDetials = getIntent();

        universityName=getDetials.getStringExtra("University");
        departmentSelected=getDetials.getStringExtra("Department");
        courseTitle=getDetials.getStringExtra("CourseName");



        final ArrayAdapter<String> ratingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scalingRate);


        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        Setting rate scale for all the dropdown
        professorRatingValue.setAdapter(ratingAdapter);
        courseRatingValue.setAdapter(ratingAdapter);
        difficultyValue.setAdapter(ratingAdapter);


        professorRatingValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){

                    finalProfessorRating = scalingRate.get(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        courseRatingValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                finalCourseRating  = scalingRate.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        difficultyValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                finalCourseDifficulty = scalingRate.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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


        final ArrayAdapter<String> courseGradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gradesOptions);

        courseGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gradesList.setAdapter(courseGradeAdapter);

        gradesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    finalGrade = gradesOptions.get(position);
                }
                else {
//                                                        Toast.makeText(CourseSurvey.this, standingsOptions.get(position), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference professorNameref = database2.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("professor");


        professorNameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                professorOptions.clear();
                professorOptions.add("");

                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    professorOptions.add(objSnapshot.getKey().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayAdapter<String> professorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, professorOptions);

        professorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        professorList.setAdapter(professorAdapter);

        professorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    finalProfessor = professorOptions.get(position);
                    Toast.makeText(CourseSurvey.this, "selected "+professorOptions.get(position), Toast.LENGTH_LONG).show();
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



                if(finalProfessor.length()>0){
                    DatabaseReference reviewProfessor = rootRef.child("professor").child(finalProfessor);


                    reviewProfessor.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //                            this is for course rating under professor

                            Integer courseRating=0,courseRatingCount=0;

                            courseRating = Integer.parseInt(dataSnapshot.child("course_rating").child("total_ratings").getValue().toString());
                            courseRatingCount= Integer.parseInt(dataSnapshot.child("course_rating").child("count").getValue().toString());


                            courseRating=courseRating+Integer.parseInt(finalCourseRating);
                            courseRatingCount=courseRatingCount+1;

                            tempCourseRatings=courseRating;
                            getTempCourseRatingsTotal=courseRatingCount;

                            Log.d("courseRating",courseRating.toString()+" "+courseRatingCount.toString());

                            //                            this is for professor rating under professor

                            Integer professorRating=0,professorRatingCount=0;

                            professorRating = Integer.parseInt(dataSnapshot.child("prof_rating").child("total_ratings").getValue().toString());
                            professorRatingCount= Integer.parseInt(dataSnapshot.child("prof_rating").child("count").getValue().toString());


                            professorRating=professorRating+Integer.parseInt(finalProfessorRating);
                            professorRatingCount=professorRatingCount+1;

                            tempProfessorRatings=professorRating;
                            getProfessorRatingsTotal= professorRatingCount;


                            Log.d("subvalues",tempProfessorRatings.toString()+" "+getProfessorRatingsTotal.toString());


//                            course diffculty rating under professor

                            Integer courseDifficultyRating=0 , courseDifficultyRatingTotal=0;

                            courseDifficultyRating = Integer.parseInt(dataSnapshot.child("difficulty").child("total_diff").getValue().toString());
                            courseDifficultyRatingTotal= Integer.parseInt(dataSnapshot.child("difficulty").child("count").getValue().toString());


                            courseDifficultyRating=courseDifficultyRating+Integer.parseInt(finalCourseDifficulty);
                            courseDifficultyRatingTotal=courseDifficultyRatingTotal+1;


                            tempCourseDifficulty=courseDifficultyRating;
                            getCourseDifficulty=courseDifficultyRatingTotal;


                            Log.d("subvaluesdifficulty",tempCourseDifficulty.toString()+" "+getCourseDifficulty.toString());



                        //           number of hours per week rating under professor

                        Integer numberofhours=0 , totalHours=0;

                            numberofhours = Integer.parseInt(dataSnapshot.child("hr_per_week").child("total_hours").getValue().toString());
                            totalHours= Integer.parseInt(dataSnapshot.child("hr_per_week").child("count").getValue().toString());


                            numberofhours=numberofhours+Integer.parseInt(hoursValue.getText().toString());
                            totalHours=totalHours+1;

                            temp_hr_weeks=numberofhours;
                            getTemp_hr_weeks=totalHours;


                        Log.d("subvaluesdifficulty",tempCourseDifficulty.toString()+" "+getCourseDifficulty.toString());




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }



                    });


                    if(tempCourseRatings!=0 && finalCourseRating.length()>0 ) {
                        Log.d("finalValues", tempCourseRatings.toString() + " " + getTempCourseRatingsTotal.toString());
                        reviewProfessor.child("course_rating").child("total_ratings").setValue(tempCourseRatings);
                        reviewProfessor.child("course_rating").child("count").setValue(getTempCourseRatingsTotal);
                    }

                    if(tempProfessorRatings!=0 && finalProfessorRating.length()>0){
                        Log.d("professorRating",tempProfessorRatings.toString()+" "+getProfessorRatingsTotal.toString());
                        reviewProfessor.child("prof_rating").child("total_ratings").setValue(tempProfessorRatings);
                        reviewProfessor.child("prof_rating").child("count").setValue(getProfessorRatingsTotal);
                    }

                    if(tempCourseDifficulty!=0 && finalCourseDifficulty.length()>0){
                        reviewProfessor.child("difficulty").child("total_diff").setValue(tempCourseDifficulty);
                        reviewProfessor.child("difficulty").child("count").setValue(getCourseDifficulty);
                    }

                    if(temp_hr_weeks!=0){
                        reviewProfessor.child("hr_per_week").child("total_hours").setValue(temp_hr_weeks);
                        reviewProfessor.child("hr_per_week").child("count").setValue(getTemp_hr_weeks);
                    }




                }else{
                    Toast.makeText(CourseSurvey.this,"Select Professor", Toast.LENGTH_LONG).show();
                }

//                adding Professor Specific details



//              adding  comment
                DatabaseReference reviewReference = rootRef.child("comments");

                final String temp_comment = commentValue.getText().toString();

                Date date = new Date();

                Comments comment = new Comments("0","0",temp_comment,date.toString(),"0","Anonymous");

                reviewReference.push().setValue(comment);


//              updating standings

                if(finalStandings!="" && finalStandings.length()>0) {

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


                DatabaseReference courseRatingReference = rootRef.child("course_rating");





                Intent objectQ = new Intent(CourseSurvey.this,CourseDetail.class);

                Toast.makeText(CourseSurvey.this,"Review posted",Toast.LENGTH_LONG).show();



//                startActivity(objectQ);
            }




        });


    }
}