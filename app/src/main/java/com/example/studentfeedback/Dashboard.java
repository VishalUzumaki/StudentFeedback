package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {


    private FirebaseAuth authObj;

    private Button logout,searchButton;

    private EditText searchString;

    private ListView coursesList;


    private  String universityName = "";


    private DataSnapshot temp_dataSnapshot;

    private Spinner selectDepartment;

    private String departmentSelected="";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        authObj = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);

        searchButton = findViewById(R.id.searchButton);

//        Spinner for filtering department
        selectDepartment =  (Spinner) findViewById(R.id.departmentSelected);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });


        coursesList = findViewById(R.id.coursesList);


        Intent uniName = getIntent();

        universityName=uniName.getStringExtra("name");

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter= new ArrayAdapter(this, R.layout.course_list_item,list);


        coursesList.setAdapter(adapter);



        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(Dashboard.this,"clicked item "+list.get(i),Toast.LENGTH_LONG).show();

                Intent openCourseDetails = new Intent(Dashboard.this,CourseDetail.class);


                openCourseDetails.putExtra("universityName", universityName);
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

                Log.d("reach","reaching here" + dataSnapshot);


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                 for(DataSnapshot courseSnapshot: snapshot.getChildren()) {

                     Log.d("reach", "inside for loop");
//                   t1.setText(t1.getText().toString() + "," +snapshot.getValue().toString());
                     list.add(courseSnapshot.getKey().toString());
                     Log.d("success", "Value is: " + courseSnapshot.getValue().toString());
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



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tempdataset",temp_dataSnapshot.toString());

                list.clear();

                for (DataSnapshot snapshot : temp_dataSnapshot.getChildren()) {

                    for(DataSnapshot courseSnapshot: snapshot.getChildren()) {

                    Log.d("reach", "tempdataset");

                    String tempSearchString = searchString.getText().toString().toLowerCase();

                    if (tempSearchString != "") {

                        if (courseSnapshot.getValue().toString().toLowerCase().indexOf(tempSearchString) != -1 || courseSnapshot.getKey().toString().toLowerCase().indexOf(tempSearchString) != -1) {
                            list.add(courseSnapshot.getKey().toString());
                            Log.d("filtered", "Value is: " + courseSnapshot.getValue().toString());
                        }

                    } else {
                        list.add(courseSnapshot.getKey().toString());
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

                departmentSpecificCourse = database.getReference().child("University").child(universityName).child(departmentSelected);

                departmentSpecificCourse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        list.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getKey().toString());
                        }

                        adapter.notifyDataSetChanged() ;
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("failed", "Failed to read value.", error.toException());
                    }
                });



                adapter.notifyDataSetChanged() ;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

        if(currentUser == null){
            startActivity(new Intent(Dashboard.this, SelectUniversity.class));
        }


//        if (currentUser != null) {
//            startActivity(new Intent( Dashboard.this,Login.class));
//        }

//         if user is already logged in
    }


    protected void logoutUser() {
        authObj.signOut();
        startActivity(new Intent(Dashboard.this, SelectUniversity.class));
    }


}