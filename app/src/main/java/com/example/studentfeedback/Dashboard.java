package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {


    private FirebaseAuth authObj;

    private Button logout;


    private ImageView img;

    private TextView t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        authObj = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });


        t1= findViewById(R.id.temp);

        img = findViewById(R.id.temp2);


        Intent ob = getIntent();

        t1.setText(ob.getStringExtra("name"));

        byte[] mBytes = ob.getByteArrayExtra("image");

        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0,mBytes.length);


        img.setImageBitmap(bitmap);



    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

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