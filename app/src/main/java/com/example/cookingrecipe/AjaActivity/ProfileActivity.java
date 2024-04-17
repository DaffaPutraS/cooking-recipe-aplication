package com.example.cookingrecipe.AjaActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookingrecipe.AjaProfile.About;
import com.example.cookingrecipe.AjaProfile.ContactUs;
import com.example.cookingrecipe.AjaProfile.FAQ;
import com.example.cookingrecipe.AjaProfile.PrivacyPolicy;
import com.example.cookingrecipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import AjaAdapter.PostAdapter;
import AjaClassGetSet.Post;
import AjaManager.SessionManager;

public class ProfileActivity extends AppCompatActivity {
    private Button about, contactUs, FAQ, policy, changePass, logout;
    private TextView username;
    private SessionManager sessionManager;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference databaseReference, databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        about = findViewById(R.id.btn_about_profile);
        contactUs = findViewById(R.id.btn_contact_profile);
        FAQ = findViewById(R.id.btn_faq_profile);
        policy = findViewById(R.id.btn_policy_profile);
        changePass = findViewById(R.id.btn_changepass_profile);
        logout = findViewById(R.id.btn_logout_profile);
        username = findViewById(R.id.txt_username_profile);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(ProfileActivity.this);

        // Cek jika pengguna belum login, arahkan ke LoginActivity
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, About.class);
                startActivity(intent);
                finish();
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ContactUs.class);
                startActivity(intent);
                finish();
            }
        });

        FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FAQ.class);
                startActivity(intent);
                finish();
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, PrivacyPolicy.class);
                startActivity(intent);
                finish();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainmainActivity.class);
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

                postAdapter = new PostAdapter(ProfileActivity.this, postList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(ProfileActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FavoriteActivity.class);
                intent.putParcelableArrayListExtra("favoritePosts", new ArrayList<>(postList));
                startActivity(intent);
            }
        });

        // Mendapatkan instance Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Mendapatkan pengguna yang saat ini login
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Mendapatkan referensi database
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users");

        // Mendapatkan data pengguna yang saat ini login berdasarkan UID
        databaseReference2.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Mengambil nilai properti "name" dari snapshot
                    String name = dataSnapshot.child("name").getValue(String.class);
                    // Mengatur nilai nama pada TextView
                    username.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(ProfileActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    // Metode untuk proses logout
    private void logout() {
        // Mendapatkan instance Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Melakukan proses logout dari Firebase Authentication
        firebaseAuth.signOut();

        // Set status login menjadi false menggunakan SessionManager
        sessionManager.setLoggedIn(false);

        // Hapus Shared Preferences yang menyimpan data login
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        // Arahkan pengguna kembali ke LoginActivity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);

        // Membersihkan semua history aktivitas
        finishAffinity();
        super.onBackPressed();

    }
}