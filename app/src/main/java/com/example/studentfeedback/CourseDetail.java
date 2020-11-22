package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView courseName,description,syllabus,prof_email,otherDescription, assign;
    private TextView avg_hrs, offered;
    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";
    private String professorSelected = "";
    private Spinner selectProfessor;
    private Button addReview,allComments;
    private List<String> professorList;
    private FirebaseDatabase database;
    PieChart pie_Chart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    private RatingBar ratingBar;
    private RatingBar ratingBar_c;
    private BarChart barChart;

    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseName = findViewById(R.id.courseName);
        syllabus = findViewById(R.id.syllabus);
        description = findViewById(R.id.description);
        pie_Chart = (PieChart) findViewById(R.id.pieChart);
        prof_email = findViewById(R.id.prof_email);
        avg_hrs = findViewById(R.id.avg_hrs);
        offered = findViewById(R.id.offered);
        barChart = findViewById(R.id.BarChart);
        assign = findViewById(R.id.assign);
        ratingBar = findViewById(R.id.prof_rating);
        ratingBar_c = findViewById(R.id.course_rating);
        otherDescription = findViewById(R.id.otherDescription);
        allComments = findViewById(R.id.allcomments);
        addReview = findViewById(R.id.addReview);

        // for piechart
        ArrayList<String> students = new ArrayList<String>();
        students.add("Freshman");
        students.add("Sophomore");
        students.add("Junior");
        students.add("Senior");
        students.add("Master's");
        students.add("PhD");

        selectProfessor =  (Spinner) findViewById(R.id.professorSpinner);

        professorList =  new ArrayList<String>();
        professorList.add(" ");

        Intent courseNameIntent = getIntent();
        universityName = courseNameIntent.getStringExtra("universityName");
        departmentSelected = courseNameIntent.getStringExtra("departmentSelected");
        String courseTitlearray[] = courseNameIntent.getStringExtra("courseName").split(" ");

        courseTitle = courseTitlearray[0]+" "+courseTitlearray[1];
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

                    Integer freshManCount=0, juniorCount =0 , seniorCount = 0, masterscount =0, phdcount =0, sophmorcount=0;
                    Integer totalStandingsCount = 0;


                    for(DataSnapshot snapshot: dataSnapshot.child("Standing").getChildren()){

                        if(snapshot.getValue().toString().equals("freshman")){
                            freshManCount=freshManCount+1;
                        }

                        if(snapshot.getValue().toString().equals("junior")){
                            juniorCount=juniorCount+1;
                        }

                        if(snapshot.getValue().toString().equals("senior")){
                            seniorCount=seniorCount+1;
                        }

                        if(snapshot.getValue().toString().equals("masters")){
                            masterscount=masterscount+1;
                        }

                        if(snapshot.getValue().toString().equals("phd")){
                            phdcount=phdcount+1;
                        }

                        if(snapshot.getValue().toString().equals("sophomor")){
                            sophmorcount=sophmorcount+1;
                        }

                        totalStandingsCount=totalStandingsCount+1;

                    };

                    pieEntries = new ArrayList<>();
                    if(freshManCount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(freshManCount*100/totalStandingsCount, "Freshmen"));
                    }
                    if(sophmorcount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(sophmorcount*100/totalStandingsCount, "Sophomore"));
                    }
                    if(juniorCount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(juniorCount*100/totalStandingsCount, "Junior"));
                    }
                    if(seniorCount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(seniorCount*100/totalStandingsCount, "Senior"));
                    }
                    if(masterscount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(masterscount*100/totalStandingsCount, "Master's"));
                    }
                    if(phdcount*100/totalStandingsCount > 0.0)
                    {
                        pieEntries.add(new PieEntry(phdcount*100/totalStandingsCount, "PhD"));
                    }

                    pieDataSet = new PieDataSet(pieEntries, "");
                    pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter());
                    pie_Chart.setData(pieData);
                    pieDataSet.setSliceSpace(2f);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(15f);
                    pie_Chart.setDrawHoleEnabled(false);
                    pie_Chart.setDrawEntryLabels(true);
                    pie_Chart.getLegend().setEnabled(false);
                    pie_Chart.setEntryLabelColor(Color.BLACK);

//                    String temp= "Freshman: "+ (freshManCount*100/totalStandingsCount) +"\n";
//                    temp= temp+ "Sophmor: "+ (sophmorcount*100/totalStandingsCount) + "\n";
//                    temp = temp+ "Junior: "+ (juniorCount*100/totalStandingsCount) + "\n";
//                    temp= temp+ "Senior: "+ (seniorCount*100/totalStandingsCount) + "\n";
//                    temp = temp+ "Masters: "+ (masterscount*100/totalStandingsCount) + "\n";
//                    temp = temp+ "PHD: "+ (phdcount*100/totalStandingsCount) + "\n";
//
//                    standings.setText(temp);

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
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar_c.setVisibility(View.VISIBLE);

                if(position!=0){
                    Log.d("Selected",professorSelected);

                    DatabaseReference professorSpecificCourse;

                    professorSpecificCourse = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("professor").child(professorSelected);

                    professorSpecificCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d("professor",dataSnapshot.toString());


                            String tempData="";
                            prof_email.setText("");
                            String peid = "Professor Email: " + dataSnapshot.child("email").getValue().toString();
                            prof_email.setText(peid);

//                            tempData="Email Id: " + dataSnapshot.child("email").getValue().toString()+"\n";

                            double totalProfessorRating=0.0;
                            double sumProfessorRating=0.0;

                            for(DataSnapshot snapshot: dataSnapshot.child("prof_rating").getChildren()){

                                sumProfessorRating=sumProfessorRating+ Integer.parseInt(snapshot.getValue().toString());
                                totalProfessorRating=totalProfessorRating+1;

                            }

//                            String avg_professor_Rating = ""+(sumProfessorRating/totalProfessorRating);

//                            tempData=tempData+ "Avg Professor Rating "+avg_professor_Rating+"\n";


                            ratingBar.setRating((float)(sumProfessorRating/totalProfessorRating));


                            double totalCourseRating=0.0;
                            double sumCourseRating=0.0;

                            for(DataSnapshot snapshot: dataSnapshot.child("course_rating").getChildren()){

                                sumCourseRating=sumCourseRating+ Integer.parseInt(snapshot.getValue().toString());
                                totalCourseRating=totalCourseRating+1;

                            }

//                            String avg_course_Rating = ""+(sumCourseRating/totalCourseRating);
//
//                            tempData=tempData+ "Avg Course Rating "+avg_course_Rating+"\n";

                            ratingBar_c.setRating((float)(sumCourseRating/totalCourseRating));

                            String ass = "Assignment Frequency: " + dataSnapshot.child("Assignment_freq").getValue().toString();
                            assign.setText("");
                            assign.setText(ass);

//                            tempData=tempData+"Assignment Frequency : "+dataSnapshot.child("Assignment_freq").getValue().toString()+"\n";
//
//                            tempData=tempData+ "Course Structure ";


                            Log.d("pqe",dataSnapshot.child("PQE").getValue().toString());

                            for (DataSnapshot objSnapshot: dataSnapshot.child("PQE").getChildren()){
                                Log.d("inside",objSnapshot.getValue().toString());
//
                                    tempData = tempData + objSnapshot.getKey().toString() +" : "+objSnapshot.getValue().toString() + "\n";
//
                            }

                            String off = "Offered during: "+dataSnapshot.child("course_offered").getValue().toString();
                            offered.setText("");
                            offered.setText(off);

                            double totalHoursSpent=0.0;
                            double sumHoursSpent=0.0;

                            for(DataSnapshot snapshot: dataSnapshot.child("hr_per_week").getChildren()){

                                sumHoursSpent=sumHoursSpent+ Integer.parseInt(snapshot.getValue().toString());
                                totalHoursSpent=totalHoursSpent+1;

                            }

                            String avg_hours_spent = ""+(sumHoursSpent/totalHoursSpent);

                            tempData=tempData+ "\n Avg Hours Spent outside class "+avg_hours_spent+"\n";


                            tempData=tempData+" \n Grade Distribution: \n";

                            String avg_hours = "Avg Hours Per Week: "+(sumHoursSpent/totalHoursSpent);
                            avg_hrs.setText("");
                            avg_hrs.setText(avg_hours);


                            Integer ACount=0, AMinusCount =0 , BCount = 0, BMinusCount =0, CCount =0, CMinusCount=0, OtherCount=0;

                            Integer totalGradesCount = 0;


                            for(DataSnapshot snapshot: dataSnapshot.child("grades").getChildren()){

                                if(snapshot.getValue().toString().equals("A")){
                                    ACount= ACount+1;
                                }

                                if(snapshot.getValue().toString().equals("A-")){
                                    AMinusCount=AMinusCount+1;
                                }

                                if(snapshot.getValue().toString().equals("B")){
                                    BCount=BCount+1;
                                }

                                if(snapshot.getValue().toString().equals("B-")){
                                    BMinusCount=BMinusCount+1;
                                }

                                if(snapshot.getValue().toString().equals("C")){
                                    CCount=CCount+1;
                                }

                                if(snapshot.getValue().toString().equals("C-")){
                                    CMinusCount=CMinusCount+1;
                                }

                                if(snapshot.getValue().toString().equals("other")){
                                    OtherCount=OtherCount+1;
                                }

                                totalGradesCount=totalGradesCount+1;

                            };

                            barEntries = new ArrayList<>();
                            barEntries.add(new BarEntry(0, ACount*100/totalGradesCount));
                            barEntries.add(new BarEntry(1, AMinusCount*100/totalGradesCount));
                            barEntries.add(new BarEntry(2, BCount*100/totalGradesCount));
                            barEntries.add(new BarEntry(3, BMinusCount*100/totalGradesCount));
                            barEntries.add(new BarEntry(4, CCount*100/totalGradesCount));
                            barEntries.add(new BarEntry(5, CMinusCount*100/totalGradesCount));
                            barEntries.add(new BarEntry(6, OtherCount*100/totalGradesCount));

                            barDataSet = new BarDataSet(barEntries, "Cells");

                            ArrayList<String> labels = new ArrayList<String>();
                            labels.add("A");
                            labels.add("A-");
                            labels.add("B");
                            labels.add("B-");
                            labels.add("C");
                            labels.add("C-");
                            labels.add("Other");

                            barData = new BarData(barDataSet);
                            barChart.setData(barData); // set the data and list of labels into chart
                            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                            barChart.animateY(1000);

                            barChart.getXAxis().setGranularityEnabled(true);
                            barChart.getXAxis().setGranularity(1.0f);
                            barChart.getXAxis().setLabelCount(barDataSet.getEntryCount());
                            barData.setBarWidth(1f);

                            barChart.setData(barData);

//                            tempData= tempData+ "A: "+ (ACount*100/totalGradesCount) +  "  A- :"+(AMinusCount*100/totalGradesCount)+"\n";
//                            tempData= tempData+ "B: "+ (BCount*100/totalGradesCount) +  "  B- :"+(BMinusCount*100/totalGradesCount)+"\n";
//                            tempData= tempData+ "C: "+ (CCount*100/totalGradesCount) +  "  C- :"+(CMinusCount*100/totalGradesCount)+"\n";
//                            tempData= tempData+ "Other: "+ (OtherCount*100/totalGradesCount);
//
//

                            tempData= tempData+ "A: "+ (ACount*100/totalGradesCount) +  "  A- :"+(AMinusCount*100/totalGradesCount)+" ";
                            tempData= tempData+ "B: "+ (BCount*100/totalGradesCount) +  "  B- :"+(BMinusCount*100/totalGradesCount)+" ";
                            tempData= tempData+ "C: "+ (CCount*100/totalGradesCount) +  "  C- :"+(CMinusCount*100/totalGradesCount)+" ";
                            tempData= tempData+ "Other: "+ (OtherCount*100/totalGradesCount);


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