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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth authObj;
    private EditText password,username;
    private Button submit,signup, back;
    private ImageView imageView;
    private TextInputLayout userT, passT;

    private String extensionDomain ="";
    private String UniversityName ="";
    private byte[] mBytes;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        imageView = findViewById(R.id.logo);
        signup= findViewById(R.id.signup);
        submit = findViewById(R.id.login);
        back = findViewById(R.id.extended_fab);
        userT = findViewById(R.id.TextFieldUser);
        passT = findViewById(R.id.TextFieldPass);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authentication");
        progressDialog.setProgress(10);
        progressDialog.setMax(100);
        progressDialog.setMessage("Validating User...");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login.this, SelectUniversity.class);
                startActivity(in);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals(""))
                {
                    username.setError("Username cannot be empty");
                }
                else if(password.getText().toString().equals(""))
                {
                    password.setError("Password cannot be empty");
                }
                else
                {
                    login();
                    progressDialog.show();
                }
            }
        });

        authObj = FirebaseAuth.getInstance();


        Intent ob = getIntent();

        extensionDomain=ob.getStringExtra("extension");
        UniversityName=ob.getStringExtra("name");

//        t1.setText(ob.getStringExtra("name"));

        mBytes = ob.getByteArrayExtra("image");

        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0,mBytes.length);


        imageView.setImageBitmap(bitmap);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Login.this, SignupPage.class);
                intent.putExtra("name",UniversityName);
                intent.putExtra("extension",extensionDomain);
                intent.putExtra("image",mBytes);

                startActivity(intent);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authObj.getCurrentUser();

        if (currentUser != null) {
            Intent openDashboard = new Intent(Login.this,Dashboard.class);
            openDashboard.putExtra("name",UniversityName);
            startActivity(openDashboard);
        }

//         if user is already logged in
    }

    protected void login() {
        String email_credential = username.getText().toString()+extensionDomain;
        String password_credential = password.getText().toString();

        authObj.signInWithEmailAndPassword(email_credential, password_credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.w("Success", "successlogin");
                            Intent openDashboard = new Intent(Login.this,Dashboard.class);
                            openDashboard.putExtra("name",UniversityName);
                            startActivity(openDashboard);
                        } else {
                            progressDialog.dismiss();
                            Log.w("Fail", "error", task.getException());
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}