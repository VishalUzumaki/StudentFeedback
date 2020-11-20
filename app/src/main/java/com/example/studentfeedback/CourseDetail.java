package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView courseName,description,syllabus,standings,otherDescription;


    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";
    private String professorSelected = "";

    private Spinner selectProfessor;

    private Button addReview;

    private List<String> professorList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseName = findViewById(R.id.courseName);
        syllabus = findViewById(R.id.syllabus);
        description = findViewById(R.id.description);
        standings = findViewById(R.id.standings);

        selectProfessor =  (Spinner) findViewById(R.id.professorSpinner);

        professorList =  new ArrayList<String>();
        professorList.add(" ");


        otherDescription = findViewById(R.id.otherDescription);


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


         database = FirebaseDatabase.getInstance();

        DatabaseReference subjectRef = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle);

        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("reach","reaching inside the subject" + dataSnapshot);

                if(dataSnapshot.getValue() != null) {
                    description.setText(dataSnapshot.child("description").getValue().toString());
                    syllabus.setText(dataSnapshot.child("syllabus").getValue().toString());

                    professorList.clear();

                    professorList.add("");

                    for (DataSnapshot objSnapshot: dataSnapshot.child("professor").getChildren()) {
                        professorList.add(objSnapshot.getKey().toString());
                    }



//                    Extracting standings total and calculating percentage
                    String standingsSubData="";

                    Integer standingsTotal = Integer.valueOf(dataSnapshot.child("Standing").child("total").getValue().toString());

                    Integer standingsFreshman = Integer.valueOf(dataSnapshot.child("Standing").child("freshman").getValue().toString());
                    Integer standingsFreshmanPercent =(standingsFreshman*100)/standingsTotal;
                    standingsSubData=standingsSubData+"Freshman "+standingsFreshmanPercent.toString()+"% \n";

                    Integer standingsJunior = Integer.valueOf(dataSnapshot.child("Standing").child("junior").getValue().toString());
                    Integer standingsJuniorPercent =(standingsJunior*100)/standingsTotal;
                    standingsSubData=standingsSubData+"Junior "+standingsJuniorPercent.toString()+"% \n";

                    Integer standingsSophomor = Integer.valueOf(dataSnapshot.child("Standing").child("sophomor").getValue().toString());
                    Integer standingsSophomorPercent =(standingsSophomor*100)/standingsTotal;
                    standingsSubData=standingsSubData+"Sophomor "+standingsSophomorPercent.toString()+"% \n";

                    Integer standingsSenior = Integer.valueOf(dataSnapshot.child("Standing").child("senior").getValue().toString());
                    Integer standingsSeniorPercent =(standingsSenior*100)/standingsTotal;
                    standingsSubData=standingsSubData+"Senior "+standingsSeniorPercent.toString()+"% \n";

                    Integer standingsMasters = Integer.valueOf(dataSnapshot.child("Standing").child("masters").getValue().toString());
                    Integer standingsMastersPercent =(standingsMasters*100)/standingsTotal;
                    standingsSubData=standingsSubData+"Masters "+standingsMastersPercent.toString()+"% \n";

                    Integer standingsPHD = Integer.valueOf(dataSnapshot.child("Standing").child("phd").getValue().toString());
                    Integer standingsPHDPercent =(standingsPHD*100)/standingsTotal;
                    standingsSubData=standingsSubData+"PHD "+standingsPHDPercent.toString()+"% \n";

                    standings.setText(standingsSubData);


                }
                else{
                    Toast.makeText(CourseDetail.this,"Inconsistent Data present for this course ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("failed", "Failed to read value.", error.toException());
            }
        });


        final ArrayAdapter<String> professorNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, professorList);


        professorNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectProfessor.setAdapter(professorNameAdapter);


        selectProfessor.setOnItemSelectedListener(CourseDetail.this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("professor","selected");
//        Toast.makeText(CourseDetail.this,"selected",Toast.LENGTH_LONG).show();


                professorSelected  = professorList.get(position);

                if(position!=0){
                    Log.d("Selected",professorSelected);

                    DatabaseReference professorSpecificCourse;

                    professorSpecificCourse = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("professor").child(professorSelected);

                    professorSpecificCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d("professor",dataSnapshot.toString());


                            String tempData="";

                            tempData="Assignment Frequency "+dataSnapshot.child("Assignment_freq").getValue().toString()+"\n";

                            tempData=tempData+ "Course Structure ";

                            Log.d("test",dataSnapshot.child("PQE").toString());

                            for (DataSnapshot objSnapshot: dataSnapshot.child("PQE").getChildren()) {
                                if(objSnapshot.getValue().toString()=="True"){
                                 tempData = tempData +  objSnapshot.getKey().toString() +" ";
                                }
                            }

                            tempData=tempData+ "\n"+"Terms this course if offered ";

//                            for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
//                                    Log.d("data",objSnapshot.toString());
//                                    tempData = tempData +  objSnapshot.getValue().toString() +" ";
//                            }


                            otherDescription.setText(tempData);

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("failed", "Failed to read value.", error.toException());
                        }

                    });
                }




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(CourseDetail.this,"not selected",Toast.LENGTH_LONG).show();
    }
}