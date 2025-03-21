package com.example.java1d;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class LoginActivity extends BackgroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        playMusic(R.raw.background_music);
//        TextView usernameHeader = findViewById(R.id.usernameHeader);
//        if (usernameHeader != null) {
//            Typeface typeface = ResourcesCompat.getFont(this, R.font.pixelify_sans);
//            usernameHeader.setTypeface(typeface);
//        }
//        else {
//            Log.e("FontError", "TextView with ID usernameHeader not found!");
//        }
    }


}
