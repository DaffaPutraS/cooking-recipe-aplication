package com.example.cookingrecipe.AjaActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingrecipe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MainmainActivity extends AppCompatActivity {

    // Deklarasi variabel
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private SessionManager sessionManager;
    private DatabaseReference databaseReference;
    private List<Post> searchList;
    private List<Post> filteredList;
    private SearchView searchView;
    private ImageView refreshButton;
    private boolean isGridView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmain);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(MainmainActivity.this);

        // Cek jika pengguna belum login, arahkan ke LoginActivity
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainmainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Mengambil data postList dari Intent jika ada
        if (getIntent().hasExtra("postList")) {
            postList = getIntent().getParcelableArrayListExtra("postList");
        }

//        // Mendapatkan referensi tombol logout
//        Button logoutButton = findViewById(R.id.logoutButton);
//
//        // Menambahkan OnClickListener ke tombol logout
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logout();
//            }
//        });

        // Mendapatkan referensi tombol favorite
        ImageView favoriteButton = findViewById(R.id.imageView4);

        // Menambahkan OnClickListener ke tombol favorite
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainmainActivity.this, FavoriteActivity.class);
                intent.putParcelableArrayListExtra("favoritePosts", new ArrayList<>(postList));
                startActivity(intent);
            }
        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainmainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainmainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Inisialisasi ImageButton
        refreshButton = findViewById(R.id.ic_refresh_mainmain);

        // Set listener untuk aksi klik pada ImageButton
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lakukan tindakan refresh di sini
                getAllPosts();
            }
        });

        recyclerView = findViewById(R.id.recyclerView_mainmain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

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

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(MainmainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        postAdapter = new PostAdapter(this, postList);
        postAdapter.setSearchList(postList);
        recyclerView.setAdapter(postAdapter);
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                // Menampilkan deskripsi lengkap post yang diklik
                Intent intent = new Intent(MainmainActivity.this, PostDetailActivity.class);
                intent.putExtra("post", post);
                intent.putExtra("id_post", post.getIdPost());
                intent.putExtra("id_creator", post.getIdCreator());
                intent.putExtra("id_subcategory", post.getIdSubcategory());
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(Post post) {
                // Menambahkan atau menghapus post dari daftar favorit
                toggleFavorite(post);
                Intent intent = new Intent(MainmainActivity.this, FavoriteActivity.class);
                intent.putParcelableArrayListExtra("postList", (ArrayList<? extends Parcelable>) postList);
                startActivity(intent);
            }

            @Override
            public void onRefreshRequested() {
                // Memuat ulang data dari database
                getAllPosts();
            }

        });

        // Inisialisasi elemen pencarian
        filteredList = new ArrayList<>();

        // Memberikan listener pada saat teks pada SearchView berubah
        searchView = findViewById(R.id.searchbar_mainmain);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Mencari list yang sesuai dengan teks yang diinputkan pada SearchView
                filteredList(newText);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Membersihkan semua history aktivitas
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
        Intent intent = new Intent(MainmainActivity.this, LoginActivity.class);
        startActivity(intent);

        // Membersihkan semua history aktivitas
        finishAffinity();
        super.onBackPressed();
    }

    // Metode untuk mendapatkan semua posting dari database
    private void getAllPosts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchView.setQuery("", false);
                postList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(MainmainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode untuk melakukan filter pada daftar posting
    private void filteredList(String text) {
        filteredList.clear();

        for (Post post : postList) {
            if (post.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(post);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(MainmainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
        }

        postAdapter.setFilteredList(filteredList);
        postAdapter.notifyDataSetChanged();
    }

    // Metode untuk menambah atau menghapus posting dari daftar favorit
    private void toggleFavorite(Post post) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("like")
                    .child(post.getIdPost());

            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Post sudah ada di daftar favorit, hapus dari daftar favorit
                        favoriteRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainmainActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainmainActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Post belum ada di daftar favorit, tambahkan ke daftar favorit
                        favoriteRef.setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainmainActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainmainActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }
}
