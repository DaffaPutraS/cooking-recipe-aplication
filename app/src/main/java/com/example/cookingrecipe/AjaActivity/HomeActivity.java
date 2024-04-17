package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cookingrecipe.AjaMenu.Fish_Tacos_al_Pastor_Activity;
import com.example.cookingrecipe.AjaMenu.Grilled_Pizza_Activity;
import com.example.cookingrecipe.AjaMenu.Pan_Seared_Salmon_Activity;
import com.example.cookingrecipe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import AjaAdapter.HomeGridAdapter;
import AjaAdapter.PostAdapter;
import AjaClassGetSet.Home;
import AjaClassGetSet.Post;

public class HomeActivity extends AppCompatActivity {
    // Deklarasi variabel
    private GridView gridView, gridView2;
    private HomeGridAdapter adapter, adapter2;
    private PostAdapter postAdapter;
    private List<Home> homeList, homeList2;
    private DatabaseReference databaseReference;
    private Button homeForgotButton;
    private List<Post> postList;
    private CardView salmon, pizza, tacos;

    // Metode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        gridView = findViewById(R.id.gridview_grid1_home);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("post");

        // Initialize homeList
        homeList = new ArrayList<>();
        homeList2 = new ArrayList<>();

        // Initialize adapter
        adapter = new HomeGridAdapter(this, homeList);
        adapter2 = new HomeGridAdapter(this, homeList2);

        // Set adapter to GridView
        gridView.setAdapter(adapter);

        // Set item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event
                Home home = homeList.get(position);
                Post post = new Post();
                post.setImage(home.getImageUrl());
                post.setAdmin(home.getAdmin());
                post.setIdPost(home.getIdPost());
                post.setIdCreator(home.getIdCreator());
                post.setIdSubcategory(home.getIdSubcategory());
                post.setIngredients(home.getIngredients());
                post.setSteps(home.getSteps());

                Intent intent = new Intent(HomeActivity.this, PostDetailActivity.class);
                intent.putExtra("post", post);  // Mengirim objek Home sebagai extra
                intent.putExtra("id_post", post.getIdPost());
                intent.putExtra("id_creator", post.getIdCreator());
                intent.putExtra("id_subcategory", post.getIdSubcategory());
                startActivity(intent);
            }
        });

        // Load data from Firebase
        loadData();

        // Set adapter to GridView2
        gridView2 = findViewById(R.id.gridview_grid2_home);
        gridView2.setAdapter(adapter2);
        // Set item click listener for gridView2
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event
                Home home2 = homeList2.get(position);
                Post post = new Post();
                post.setImage(home2.getImageUrl());
                post.setAdmin(home2.getAdmin());
                post.setIdPost(home2.getIdPost());
                post.setIdCreator(home2.getIdCreator());
                post.setIdSubcategory(home2.getIdSubcategory());
                post.setIngredients(home2.getIngredients());
                post.setSteps(home2.getSteps());

                Intent intent = new Intent(HomeActivity.this, PostDetailActivity.class);
                intent.putExtra("post", post);  // Mengirim objek Home sebagai extra
                intent.putExtra("id_post", post.getIdPost());
                intent.putExtra("id_creator", post.getIdCreator());
                intent.putExtra("id_subcategory", post.getIdSubcategory());
                startActivity(intent);
            }
        });

        // Load data for GridView2
        loadDataGrid2();

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainmainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Mengambil data postList dari Intent jika ada
        if (getIntent().hasExtra("postList")) {
            postList = getIntent().getParcelableArrayListExtra("postList");
        }

        postList = new ArrayList<>();

        // Mendapatkan referensi database
        databaseReference = FirebaseDatabase.getInstance().getReference("post");

        // Mendapatkan data dari database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter = new PostAdapter(HomeActivity.this, postList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(HomeActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FavoriteActivity.class);
                intent.putParcelableArrayListExtra("favoritePosts", new ArrayList<>(postList));
                startActivity(intent);
            }
        });

        ImageView imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        salmon = findViewById(R.id.cardview_pan_seared_salmon);
        salmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Pan_Seared_Salmon_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        tacos = findViewById(R.id.cardview_fish_tacos_al_pastor);
        tacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Fish_Tacos_al_Pastor_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        pizza = findViewById(R.id.cardview_grilled_pizza);
        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Grilled_Pizza_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Metode onBackPressed
    @Override
    public void onBackPressed() {
        // Membersihkan semua history aktivitas
        finishAffinity();
        super.onBackPressed();
    }

    // Metode loadData
    private void loadData() {
        Query query = databaseReference.orderByChild("id_post").startAt("PST1").endAt("PST6");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Home home = snapshot.getValue(Home.class);
                    if (home != null && (home.getIdPost().equals("PST11") || home.getIdPost().equals("PST3") || home.getIdPost().equals("PST4"))) {
                        Log.d("GridView", "id_post: " + home.getIdPost());
                        homeList.add(home);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode loadDataGrid2
    private void loadDataGrid2() {
        Query query = databaseReference.orderByChild("id_post").startAt("PST1").endAt("PST6");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeList2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Home home = snapshot.getValue(Home.class);
                    if (home != null && (home.getIdPost().equals("PST2") || home.getIdPost().equals("PST5") || home.getIdPost().equals("PST6"))) {
                        Log.d("GridView2", "id_post: " + home.getIdPost());
                        homeList2.add(home);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
