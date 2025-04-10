package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class InventoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inventory_page, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getUserInfo();

        // Change Class
        ImageButton changeClassBtn = view.findViewById(R.id.changeClassBtn);
        changeClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeroSelectionActivity.class);
                startActivity(intent);
        //  Toast.makeText(getActivity(),"Changing class", Toast.LENGTH_SHORT).show();
            }
        });

        // Change avatar
        ImageView avatar_image = view.findViewById(R.id.avatar);
        String avatar = user.getHeroClass().toLowerCase();
        String imageResourceName = "avatar_" + avatar;
        avatar_image.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));

        return view;
    }
}