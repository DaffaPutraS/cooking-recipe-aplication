package com.example.cookingrecipe.AjaMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingrecipe.AjaActivity.MainmainActivity;
import com.example.cookingrecipe.R;

public class Pan_Seared_Salmon_Activity extends AppCompatActivity {

    ImageView arrowLeft, favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_seared_salmon);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        arrowLeft = findViewById(R.id.imageview_arrow_left_food_detail);
        favorite = findViewById(R.id.imageview_favorite_food_detail);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite_hover));
                Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pan_Seared_Salmon_Activity.this, MainmainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}