package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends BackgroundActivity{

    EditText emailInput, passwordInput , repasswordInput,usernameInput;

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
                //TODO 1. need to check if username/email exists in db - if it does, prompt to differ

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String rePassword = repasswordInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password) | (TextUtils.isEmpty(rePassword))) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!password.equals(rePassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                //TODO 2 : HASH PASSWORD before storing into DB.
                //new user has all the game values set to ZERO and className as NIL.
                User newUser = new User(username, email, password,"NIL",0,0,0,0,null);


                DBHelper db_helper = new DBHelper(SignUpActivity.this);
                long result = db_helper.createUser(newUser);
                db_helper.close();

                if (result != -1) {
                    Toast.makeText(SignUpActivity.this, "Success! Registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUpActivity.this, "Error saving to database", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
