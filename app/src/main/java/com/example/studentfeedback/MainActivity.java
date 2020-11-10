package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;

    ImageView img;

    private int LoggedIn = 0;

    private FirebaseAuth authObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        authObj = FirebaseAuth.getInstance();

        img=findViewById(R.id.logo);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob = new Intent(MainActivity.this,Login.class);
                startActivity(ob);
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, SelectUniversity.class);

                if(LoggedIn ==0 ) //that is not logged in
                {
                    startActivity(intent);
                }
            }
        },2000);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

        if (currentUser != null) {
            LoggedIn=1;
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        }

//         if user is already logged in
    }



}