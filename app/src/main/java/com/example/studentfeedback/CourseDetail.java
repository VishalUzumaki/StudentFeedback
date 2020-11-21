package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    private Button addReview,allComments;
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


        allComments = findViewById(R.id.allcomments);


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


        allComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent object = new Intent(CourseDetail.this,AllComments.class);

                object.putExtra("University",universityName);
                object.putExtra("Department",departmentSelected);
                object.putExtra("CourseName",courseTitle);

                startActivity(object);
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addSurvey = new Intent(CourseDetail.this,CourseSurvey.class);

                addSurvey.putExtra("University",universityName);
                addSurvey.putExtra("Department",departmentSelected);
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

                            tempData="Email Id: " + dataSnapshot.child("email").getValue().toString()+"\n";

                            float course_professor_total=Float.parseFloat(dataSnapshot.child("prof_rating").child("total_ratings").getValue().toString());
                            float course_professor_count=Float.parseFloat(dataSnapshot.child("prof_rating").child("count").getValue().toString());

                            float avg_ratings_professor= course_professor_total/course_professor_count;

                            tempData=tempData+"Average Professor Rating: " + String.valueOf(avg_ratings_professor)+"\n";

                            float course_rating_total=Float.parseFloat(dataSnapshot.child("course_rating").child("total_ratings").getValue().toString());
                            float course_rating_count=Float.parseFloat(dataSnapshot.child("course_rating").child("count").getValue().toString());

                            float avg_ratings= course_rating_total/course_rating_count;

                            tempData=tempData+"Average Course Rating: " + String.valueOf(avg_ratings)+"\n";

                            tempData=tempData+"Assignment Frequency : "+dataSnapshot.child("Assignment_freq").getValue().toString()+"\n";

                            tempData=tempData+ "Course Structure ";

                            Log.d("test",dataSnapshot.child("PQE").toString());

                            Log.d("pqe",dataSnapshot.child("PQE").getValue().toString());

                            for (DataSnapshot objSnapshot: dataSnapshot.child("PQE").getChildren()){
                                Log.d("inside",objSnapshot.getValue().toString());
//                                if (objSnapshot.getValue().toString() == "yes" || objSnapshot.getValue().toString() == "True" ) {
                                    tempData = tempData + objSnapshot.getKey().toString() +" : "+objSnapshot.getValue().toString() + " ";
//                                }
//                                else{
//                                    tempData = tempData + "Nope" + " ";
//                                }
                            }

                            tempData=tempData+ "\n"+"Offered during : "+dataSnapshot.child("course_offered").getValue().toString();

                            float hours_perweek_total=Float.parseFloat(dataSnapshot.child("hr_per_week").child("total_hours").getValue().toString());
                            float hours_perweek_count=Float.parseFloat(dataSnapshot.child("hr_per_week").child("count").getValue().toString());

                            float avg_hours= hours_perweek_total/hours_perweek_count;

                            tempData=tempData+ "\n"+"Number of Hours Spent outside class/week : "+ String.valueOf(avg_hours);

                            tempData=tempData+"\n"+"\n"+"Grade Distribution: ";


                            float course_grade_total =  Float.parseFloat(dataSnapshot.child("grades").child("total").getValue().toString());

                            for (DataSnapshot objSnapshot: dataSnapshot.child("grades").getChildren()){

                                float temp_rating =  Float.parseFloat(objSnapshot.getValue().toString());
                                float percent = (temp_rating*100)/course_grade_total;

                                String tempGrade = objSnapshot.getKey().toString();

                                Log.d("tempGrade",tempGrade);

                                if(!tempGrade.equals("total")){
                                    tempData = tempData + "\n"+ tempGrade +" : " +  String.valueOf(percent)+" ";
                                }


                            }


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