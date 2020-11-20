package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);

        commentsList = findViewById(R.id.commentsList);

        commentsListData = new ArrayList<>();
        commentsListData.add("");


        idofComments = new ArrayList<>();
        idofComments.add("0");


        final ArrayAdapter commentsAdapter= new ArrayAdapter(this, R.layout.course_list_item,commentsListData);

        commentsList.setAdapter(commentsAdapter);

        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AllComments.this,"yes",Toast.LENGTH_LONG).show();

                Log.d("called","setonitem selected");

                String commentSelected = commentsListData.get(position);

                if(position!=0) {

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

        Intent baseDetails = getIntent();

        universityName=baseDetails.getStringExtra("University");
        departmentSelected = baseDetails.getStringExtra("Department");
        courseTitle = baseDetails.getStringExtra("CourseName");

        database = FirebaseDatabase.getInstance();


        DatabaseReference comments = database.getReference().child("University").child(universityName).child(departmentSelected).child(courseTitle).child("comments");

        Log.d("comments",comments.toString());

        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentsListData.clear();
                idofComments.clear();

                commentsListData.add("");
                idofComments.add("0");

                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {

                    Log.d("comments details",objSnapshot.getValue().toString());

                    Integer strikeCount = Integer.parseInt(objSnapshot.child("strike").getValue().toString());
                    if(strikeCount<3) {
                        commentsListData.add(objSnapshot.child("text").getValue().toString());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("called","setonitem selected");

        String commentSelected = commentsListData.get(position);

        if(position!=0) {

            Intent object = new Intent(AllComments.this, IndividualComment.class);

            object.putExtra("UniversityName", universityName);
            object.putExtra("Department", departmentSelected);
            object.putExtra("CouseSelected", courseTitle);
            object.putExtra("Comment", idofComments.get(position));

            startActivity(object);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}