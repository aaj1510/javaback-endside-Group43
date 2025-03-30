package com.example.java1d;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HeroSelectionActivity extends BackgroundActivity{
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        ImageButton selectWarrior = findViewById(R.id.warrior_poster);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_page);
        getClassData();
//        selectWarrior.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void getClassData(){
        TextView warrior_traits = findViewById(R.id.warrior_traits);
        TextView mage_traits = findViewById(R.id.mage_traits);
        TextView archer_traits = findViewById(R.id.archer_traits);
        TextView pirate_traits = findViewById(R.id.pirate_traits);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Class").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()){
                    Log.e("Firebase", "Failed getting data", task.getException());
                }
                else {
                    Log.d("Firebase", String.valueOf(task.getResult().getValue()));
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot child: snapshot.getChildren()){
                        String className = child.getKey();
                        String traits = String.valueOf(child.child("traits").getValue());
                        if(className.equals("Warrior")){
                            warrior_traits.setText(traits);
                        } else if (className.equals("Mage")){
                            mage_traits.setText(traits);
                        } else if (className.equals("Archer")) {
                            archer_traits.setText(traits);
                        } else if (className.equals("Pirate")){
                            pirate_traits.setText(traits);
                        } else {
                            Toast.makeText(HeroSelectionActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

//  public Bitmap decodeImage(String encodedString){
//        if(encodedString != null){
//            String encodedValue = "data:image/png;base64,";
//            String base64String = encodedString.substring(encodedValue.indexOf(",") + 1);
//            Log.d("Image", base64String);
//            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
//            Log.d("Bitmap", "Width: " + decodedByte.getWidth() + ", Height: " + decodedByte.getHeight());
//            Log.d("Density", "Screen density: " + getResources().getDisplayMetrics().density);
//            return decodedByte;
//        } else {
//            return null;
//        }
//
//  }
}
