package com.example.java1d;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class LoginActivity extends BackgroundActivity {
    EditText usernameInput, passwordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        playMusic(R.raw.background_music);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
               startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(usernameInput.getText());
                String password = String.valueOf(passwordInput.getText().toString());

                DBHelper db_helper = new DBHelper(LoginActivity.this);

                User userExists = db_helper.checkUserExists(username,password);

                if (userExists != null){
                    Intent newIntent;

                    if(userExists.getClassName().trim().equals("NIL")){
                        //newIntent = new Intent(LoginActivity.this,AvatarChoosing.class);
                        //Need to change to intent when activity is added
                        setContentView(R.layout.hero_selection_page);
                        //Log.d("Login", "ClassName: '" + userExists.getClassName() + "'");
                        //System.out.println(userExists.getClassName());
                    }
                    else {
                        //System.out.println(userExists.getClassName());
                        newIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(newIntent);
                    }

                    // Pass the user details using intent extras
                    //newIntent.putExtra("uid", userExists.getUserId());
                    //newIntent.putExtra("username", userExists.getUsername());
                    //newIntent.putExtra("email", userExists.getEmail());
                    //newIntent.putExtra("className", userExists.getClassName());
                    //startActivity(newIntent);

                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
