package com.example.studentfeedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SelectUniversity extends AppCompatActivity {

    RecyclerView universityNames;
    UniNameAdapter uniNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_university);


        universityNames = findViewById(R.id.recycleViewList);
        universityNames.setLayoutManager(new LinearLayoutManager(this));

        uniNameAdapter = new UniNameAdapter( SelectUniversity.this,getNamesList());

        universityNames.setAdapter(uniNameAdapter);

    }


    private ArrayList<Model> getNamesList(){

        ArrayList<Model> models = new ArrayList<>();

        Model m =new Model();

        m.setUniname("Indiana University");
        m.setImage(R.drawable.iulogo);
        m.setExtension("@iu.edu");


        models.add(m);

        m =new Model();

        m.setUniname("NYU University");
        m.setImage(R.drawable.nyu);
        m.setExtension("@nyu.edu");

        models.add(m);

        m =new Model();

        m.setUniname("Penn State University");
        m.setImage(R.drawable.penn);
        m.setExtension("@psu.edu");

        models.add(m);

        return models;


    }


}