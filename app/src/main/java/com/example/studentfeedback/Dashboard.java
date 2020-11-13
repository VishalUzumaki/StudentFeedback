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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class Dashboard extends AppCompatActivity {


    private FirebaseAuth authObj;

    private Button logout;

    private EditText t1;

    private ListView coursesList;


    private  String universityName = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("University").child(universityName);


        t1=findViewById(R.id.search);

        t1.setText(universityName.toString());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                list.clear();

                Log.d("reach","reaching here" + dataSnapshot);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("reach","inside for loop");

//                    t1.setText(t1.getText().toString() + "," +snapshot.getValue().toString());
                    list.add(snapshot.getKey().toString());
                    Log.d("success", "Value is: " + snapshot.getValue().toString());
                }

                adapter.notifyDataSetChanged() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("failed", "Failed to read value.", error.toException());
            }
        });

//        myRef.setValue("Hello, World!");


//        Intent ob = getIntent();
//
//        t1.setText(ob.getStringExtra("name"));
//
//        byte[] mBytes = ob.getByteArrayExtra("image");
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0,mBytes.length);
//
//
//        img.setImageBitmap(bitmap);



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