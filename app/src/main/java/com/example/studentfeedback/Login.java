package com.example.studentfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth authObj;
    private EditText password,username;
    private Button submit,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = findViewById(R.id.password);
        username = findViewById(R.id.username);


        signup= findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignupPage.class));
            }
        });


        submit = findViewById(R.id.login);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        authObj = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(Login.this, Dashboard.class));
        }

//         if user is already logged in
    }

    protected void login() {
        String email_credential = username.getText().toString();
        String password_credential = password.getText().toString();

        authObj.signInWithEmailAndPassword(email_credential, password_credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.w("Success", "successlogin");
                            startActivity(new Intent(Login.this,Dashboard.class));
                        } else {
                            Log.w("Fail", "error", task.getException());
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }





}