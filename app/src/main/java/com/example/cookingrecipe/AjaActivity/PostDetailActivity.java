package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingrecipe.AjaProfile.PrivacyPolicy;
import com.example.cookingrecipe.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import AjaClassGetSet.Post;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView imageView, backImage;
    private TextView nameTextView, adminTextView, admin2TextView, idCreatorTextView, idPostTextView, idSubcategoryTextView, ingredientsTextView, ingredients2TextView, stepsTextView, steps2TextView;
    private boolean isLiked = false;
    private DatabaseReference likeRef; // Referensi ke tabel 'like' di Firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        imageView = findViewById(R.id.img_imageview_postdetail);
        nameTextView = findViewById(R.id.txt_title_postdetail);
        adminTextView = findViewById(R.id.txt_admin_postdetail);
        admin2TextView = findViewById(R.id.txt_admintext_postdetail);
        ingredientsTextView = findViewById(R.id.txt_ingredients_postdetail);
        ingredients2TextView = findViewById(R.id.txt_ingredientstext_postdetail);
        stepsTextView = findViewById(R.id.txt_steps_postdetail);
        steps2TextView = findViewById(R.id.txt_stepstext_postdetail);
        backImage = findViewById(R.id.imageview_arrow_left_food_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("post")) {
            Post post = intent.getParcelableExtra("post");
            /*String idPost = intent.getStringExtra("id_post");
            String idCreator = intent.getStringExtra("id_creator");
            String idSubcategory = intent.getStringExtra("id_subcategory");*/

            if (post != null) {
                Picasso.get().load(post.getImageUrl()).into(imageView);
                nameTextView.setText(post.getName());
                admin2TextView.setText(post.getAdmin());

                // Mengganti "\n" dengan karakter garis baru jika ingredients tidak null
                if (post.getIngredients() != null) {
                    String ingredients = post.getIngredients().replace("\\n", "\n");
                    ingredients2TextView.setText(ingredients);
                }

                // Mengganti "\n" dengan karakter garis baru jika steps tidak null
                if (post.getSteps() != null) {
                    String steps = post.getSteps().replace("\\n", "\n");
                    steps2TextView.setText(steps);
                }
            }
        }

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, MainmainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Membersihkan semua history aktivitas
        Intent intent = new Intent(PostDetailActivity.this, MainmainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
