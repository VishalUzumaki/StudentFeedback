package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class underConstruction extends AppCompatActivity {

    private Toolbar toolbar;
    ActionMenuItemView logout;
    private FirebaseAuth authObj;
    private String UniversityName = "";
    private String check = "";
    private TextView exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);

        authObj = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.appBar);
        exp = findViewById(R.id.explain);

        Intent ob = getIntent();

        UniversityName = ob.getStringExtra("name");
        check = ob.getStringExtra("check");

        if(check.equals("sug"))
        {
            String s = "A course suggestion feature presenting the user with a list of course based on their preferences";
            exp.setText(s);
        }
        else if(check.equals("qa"))
        {
            String s = "A discussion forum for user to discuss and help other users with questions they have regarding courses";
            exp.setText(s);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), options.class);
                in.putExtra("name",UniversityName);
                startActivity(in);
                finish();
            }
        });

        logout = findViewById(R.id.logout);
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