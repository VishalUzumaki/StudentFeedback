package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllComments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> commentsListData,idofComments;
    private FirebaseDatabase database;
    private ListView commentsList;

    private String universityName="";
    private String courseTitle="";
    private String departmentSelected="";

    private FirebaseAuth authObj;
    private Toolbar toolbar;
    ActionMenuItemView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);

        commentsList = findViewById(R.id.commentsList);

        commentsListData = new ArrayList<>();
        commentsListData.add("");


        idofComments = new ArrayList<>();
        idofComments.add("0");

        toolbar = findViewById(R.id.appBar_com);
        logout = findViewById(R.id.logout);

        Intent baseDetails = getIntent();

        universityName=baseDetails.getStringExtra("University");
        departmentSelected = baseDetails.getStringExtra("Department");
        courseTitle = baseDetails.getStringExtra("CourseName");

        authObj = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checker1", courseTitle);
                Intent in = new Intent(getApplicationContext(), CourseDetail.class);
                in.putExtra("universityName",universityName);
                in.putExtra("departmentSelected",departmentSelected);
                in.putExtra("courseName",courseTitle);
                startActivity(in);
                finish();
            }
        });


        final ArrayAdapter commentsAdapter= new ArrayAdapter(this, R.layout.simple_all_comments_layout, commentsListData);

        commentsList.setAdapter(commentsAdapter);

        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AllComments.this,"yes",Toast.LENGTH_LONG).show();

                Log.d("called", "setonitem selected");

                String commentSelected = commentsListData.get(position);

                if (position != 0) {

                    Intent object = new Intent(AllComments.this, IndividualComment.class);

                    object.putExtra("UniversityName", universityName);
                    object.putExtra("Department", departmentSelected);
                    object.putExtra("CouseSelected", courseTitle);
                    object.putExtra("Comment", idofComments.get(position));

                    startActivity(object);
                }
            }
        });

//        commentsList.setOnItemSelectedListener(AllComments.this);

        database = FirebaseDatabase.getInstance();


        DatabaseReference comments = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("comments");

        Log.d("comments",comments.toString());

        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentsListData.clear();
                idofComments.clear();

//                commentsListData.add("");
//                idofComments.add("0");

                int count = 0;
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {

                    count = count + 1;

                    Log.d("comments details",objSnapshot.getValue().toString());

                    Integer strikeCount = Integer.parseInt(objSnapshot.child("strike").getValue().toString());
                    if(strikeCount<3) {
                        String tempData="comment: " + count;
//                        tempData=objSnapshot.child("text").getValue().toString();
//
//                        tempData=tempData+"\n Upvote :"+ objSnapshot.child("upvote").getValue().toString()+ "     Detailed info->";


                        commentsListData.add(tempData);
                        idofComments.add(objSnapshot.getKey().toString());
                        }
                }

                commentsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    protected void logoutUser() {
        authObj.signOut();
        Intent in = new Intent(getApplicationContext(), SelectUniversity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("called","setonitem selected");

        String commentSelected = commentsListData.get(position);



        Intent object = new Intent(AllComments.this, IndividualComment.class);

        object.putExtra("UniversityName", universityName);
        object.putExtra("Department", departmentSelected);
        object.putExtra("CouseSelected", courseTitle);
        object.putExtra("Comment", idofComments.get(position));

        startActivity(object);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}