package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BackgroundActivity {

    EditText emailInput, passwordInput, repasswordInput, usernameInput;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        TextView loginText = findViewById(R.id.login_text);
        emailInput = findViewById(R.id.email_input);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        repasswordInput = findViewById(R.id.confirm_password_input);
        Button btnReg = findViewById(R.id.create_button);
        mAuth = FirebaseAuth.getInstance();
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });





        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 1. need to check if username exists in db - if it does, prompt to differ <-IMPT

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String rePassword = repasswordInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Invalid Email Pattern!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Enter username", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password) | (TextUtils.isEmpty(rePassword))) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

                } else if (!password.equals(rePassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                } else {

                    //create user
                    mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //store into db
                            String uid = mAuth.getUid();
                            User user = new User(uid, username, email);
                            Map<String, Object> userValues = user.toMap();

                            //set data to db
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(uid).setValue(userValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // data added on db
                                            Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //data failed to add on db
                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Toast.makeText(SignUpActivity.this, "Authenticated!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }
}
