package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseSearch extends AppCompatActivity {


    private FirebaseAuth authObj;
    private Button logout_,searchButton;
    private EditText searchString;
    private ListView coursesList;
    private  String universityName = "";
    private DataSnapshot temp_dataSnapshot;
    private Spinner selectDepartment;
    private String departmentSelected="";
    ActionMenuItemView logout;
    Toolbar toolbar;

    ProgressDialog progressDialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

        authObj = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        searchButton = findViewById(R.id.searchButton);
        // Spinner for filtering department
        selectDepartment =  (Spinner) findViewById(R.id.departmentSelected);
        toolbar = findViewById(R.id.appBar_cs);
        coursesList = findViewById(R.id.coursesList);

        Intent uniName = getIntent();
        universityName=uniName.getStringExtra("name");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), options.class);
                in.putExtra("name",universityName);
                startActivity(in);
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Information");
        progressDialog.setProgress(10);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        final ArrayList<String> list = new ArrayList<>();
        // this value will be useful for the course details page to get the department name if All is selected
        final ArrayList<String> placeholderforDepartmentName = new ArrayList<>();
        final ArrayAdapter adapter= new ArrayAdapter(this, R.layout.course_list_item, R.id.label, list);
        coursesList.setAdapter(adapter);

        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openCourseDetails = new Intent(CourseSearch.this,CourseDetail.class);
                openCourseDetails.putExtra("universityName", universityName);

                Log.d("placeholder","for department|"+placeholderforDepartmentName.toString());

                if(departmentSelected == "All")
                {
                    openCourseDetails.putExtra("departmentSelected",placeholderforDepartmentName.get(i));
                }
                else
                {
                    openCourseDetails.putExtra("departmentSelected",departmentSelected);
                }

                openCourseDetails.putExtra("courseName",list.get(i));
                startActivity(openCourseDetails);
            }
        });

        //        getting University Name for course search
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference departmentNames = database.getReference().child("University").child(universityName);
        Log.d("test",departmentNames.toString());

        final List<String> departmentsList = new ArrayList<String>();

        departmentsList.add("All"); //This is for All
        departmentNames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {

                    Log.d("test","data"+objSnapshot.getKey().toString());

                    departmentsList.add(objSnapshot.getKey().toString());

                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }
        });


        ArrayAdapter<String> departmentNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentsList);


        departmentNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectDepartment.setAdapter(departmentNameAdapter);

        DatabaseReference myRef;
//      this is the reference of university name from where we will get list of subject names
        myRef = database.getReference().child("University").child(universityName);



//        if(departmentSelected == "") {
//
//        }
//        else{
//          myRef = database.getReference().child("University").child(universityName).child(departmentSelected);
//        }

        searchString=findViewById(R.id.search);

        // list of courses
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                temp_dataSnapshot = dataSnapshot;

                list.clear();

                placeholderforDepartmentName.clear();

                Log.d("reach","reaching here" + dataSnapshot);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                 String departmentPlaceHolder = snapshot.getKey().toString();

                 for(DataSnapshot courseSnapshot: snapshot.getChildren()) {

                     placeholderforDepartmentName.add(departmentPlaceHolder);

                     Log.d("reach", "inside for loop");
//                   t1.setText(t1.getText().toString() + "," +snapshot.getValue().toString());
                     list.add(courseSnapshot.getKey().toString()+" "+courseSnapshot.child("courseName").getValue().toString());
                     Log.d("success", "Value is: " + courseSnapshot.child("courseName").toString());
                 }

                }
                progressDialog.dismiss();
                adapter.notifyDataSetChanged() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("failed", "Failed to read value.", error.toException());
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tempdataset",temp_dataSnapshot.toString());

                placeholderforDepartmentName.clear();

                list.clear();

                for (DataSnapshot snapshot : temp_dataSnapshot.getChildren()) {

                    String departmentPlaceHolder = snapshot.getKey().toString();

                    for(DataSnapshot courseSnapshot: snapshot.getChildren()) {

                        placeholderforDepartmentName.add(departmentPlaceHolder);

                        Log.d("reach", "tempdataset");

                        String tempSearchString = searchString.getText().toString().toLowerCase();

                        if (tempSearchString != "") {

                            if (courseSnapshot.getValue().toString().toLowerCase().indexOf(tempSearchString) != -1 || courseSnapshot.getKey().toString().toLowerCase().indexOf(tempSearchString) != -1) {
                                list.add(courseSnapshot.getKey().toString()+" "+courseSnapshot.child("courseName").getValue().toString());
                                Log.d("filtered", "Value is: " + courseSnapshot.getValue().toString());
                            }

                        } else {
                            list.add(courseSnapshot.getKey().toString()+" "+courseSnapshot.child("courseName").getValue().toString());
                        }
    //                    Log.d("success", "Value is: " + snapshot.getValue().toString());
                    }
                }

                adapter.notifyDataSetChanged() ;

            }
        });


        selectDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                departmentSelected = departmentsList.get(position);


                Log.d("departmentSelected",departmentSelected);


                DatabaseReference departmentSpecificCourse;

                if(departmentSelected != "All")
                {
                    departmentSpecificCourse = database.getReference().child("University").child(universityName).child(departmentSelected);



                    departmentSpecificCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                            temp_dataSnapshot = dataSnapshot;

                            list.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getKey().toString()+" "+snapshot.child("courseName").getValue().toString());
                            }

                            adapter.notifyDataSetChanged() ;
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("failed", "Failed to read value.", error.toException());
                        }
                    });
                }
                else{


                    departmentSpecificCourse =  database.getReference().child("University").child(universityName);


                    departmentSpecificCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                            temp_dataSnapshot = dataSnapshot;

                            list.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for(DataSnapshot courseSnapshot: snapshot.getChildren()) {
                                    list.add(courseSnapshot.getKey().toString()+" "+courseSnapshot.child("courseName").getValue().toString());
                                }
                            }
                            adapter.notifyDataSetChanged() ;
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("failed", "Failed to read value.", error.toException());
                        }
                    });


                }
                adapter.notifyDataSetChanged() ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    protected void logoutUser() {
        authObj.signOut();
        Intent in = new Intent(CourseSearch.this, SelectUniversity.class);
        startActivity(in);
        finish();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = authObj.getCurrentUser();
//
//        if(currentUser == null){
//            startActivity(new Intent(CourseSearch.this, SelectUniversity.class));
//        }


//        if (currentUser != null) {
//            startActivity(new Intent( CourseSearch.this,Login.class));
//        }

//         if user is already logged in
//    }

}