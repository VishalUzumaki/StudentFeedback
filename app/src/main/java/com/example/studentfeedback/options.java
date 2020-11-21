package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class options extends AppCompatActivity {


    private TextView toolbar_title;
    private Button search, suggestion, qa;
    ActionMenuItemView logout;
    private FirebaseAuth authObj;
    private  String UniversityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        toolbar_title = findViewById(R.id.toolbar_title);
        search = findViewById(R.id.courseSearch);
        suggestion = findViewById(R.id.courseSuggestion);
        qa = findViewById(R.id.QnA);
        logout = findViewById(R.id.logout);

        authObj = FirebaseAuth.getInstance();

        Intent ob = getIntent();

        UniversityName = ob.getStringExtra("name");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), CourseSearch.class);
                in.putExtra("name",UniversityName);
                startActivity(in);
            }
        });

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), underConstruction.class);
                in.putExtra("name",UniversityName);
                startActivity(in);
            }
        });

        qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), underConstruction.class);
                in.putExtra("name",UniversityName);
                startActivity(in);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

    }

    protected void logoutUser() {
        authObj.signOut();
        startActivity(new Intent(getApplicationContext(), SelectUniversity.class));
        finish();
    }
}