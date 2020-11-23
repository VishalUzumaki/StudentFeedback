package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SelectUniversity extends AppCompatActivity {

    RecyclerView universityNames;
    UniNameAdapter uniNameAdapter;
    private FirebaseAuth authObj;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_university);



        authObj = FirebaseAuth.getInstance();

//        setting the recycler view for selecting university
//        on selecting the course we will be appending the domain name @iu.edu for Indiana University
//        @nyu.edu for NYU university, etc.

        universityNames = findViewById(R.id.recycleViewList);
        universityNames.setLayoutManager(new LinearLayoutManager(this));

        uniNameAdapter = new UniNameAdapter( SelectUniversity.this,getNamesList());
        universityNames.setAdapter(uniNameAdapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

//        checking if the user has already logged in and if such then navigating him/her to the Dashboard
        if(currentUser != null){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            String uniName=pref.getString("University", null);

            Intent openDashboard = new Intent(SelectUniversity.this, options.class);
            openDashboard.putExtra("name",uniName);
            startActivity(openDashboard);
            finish();
        }

    }

    private ArrayList<Model> getNamesList(){

        ArrayList<Model> models = new ArrayList<>();

        Model m =new Model();

        m.setUniname("Indiana University Bloomington");
        m.setImage(R.drawable.iulogo);
        m.setExtension("@iu.edu");


        models.add(m);

        m =new Model();

        m.setUniname("New York University");
        m.setImage(R.drawable.nyu);
        m.setExtension("@nyu.edu");

        models.add(m);

        m =new Model();

        m.setUniname("Pennsylvania State University");
        m.setImage(R.drawable.penn);
        m.setExtension("@psu.edu");

        models.add(m);

        return models;


    }


}