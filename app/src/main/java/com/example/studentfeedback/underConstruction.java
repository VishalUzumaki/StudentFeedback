package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class underConstruction extends AppCompatActivity {

    private Toolbar toolbar;
    ActionMenuItemView logout;
    private FirebaseAuth authObj;
    private String UniversityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);

        authObj = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.appBar);

        Intent ob = getIntent();

        UniversityName = ob.getStringExtra("name");

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