package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupPage extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText password,username;
    private Button signup, back;

    private ImageView imageView;

    private String extensionDomain ="";
    private String UniversityName ="";
    private byte[] mBytes;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        imageView = findViewById(R.id.imageView);
        signup= findViewById(R.id.signup);
        back = findViewById(R.id.extended_fab);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authentication");
        progressDialog.setProgress(10);
        progressDialog.setMax(100);
        progressDialog.setMessage("Validating User...");

        Intent ob = getIntent();

        extensionDomain=ob.getStringExtra("extension");
        UniversityName=ob.getStringExtra("name");
        mBytes = ob.getByteArrayExtra("image");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SignupPage.this, Login.class);
                in.putExtra("name",UniversityName);
                in.putExtra("extension",extensionDomain);
                in.putExtra("image",mBytes);
                startActivity(in);
            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0,mBytes.length);
        imageView.setImageBitmap(bitmap);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                signup();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // if user is already logged in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            Intent openDashboard = new Intent(SignupPage.this, CourseSearch.class);
//            openDashboard.putExtra("name",UniversityName);
//            startActivity(openDashboard);
//        }
//    }



    protected void signup() {
        String email_credential = username.getText().toString() + extensionDomain;
        String password_credential = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email_credential, password_credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent in = new Intent(SignupPage.this, Login.class);
                            in.putExtra("name",UniversityName);
                            startActivity(in);
                            //startActivity(new Intent(SignupPage.this, CourseSearch.class));
                            Toast.makeText(SignupPage.this, "Registration Successful.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupPage.this, "Registration failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}