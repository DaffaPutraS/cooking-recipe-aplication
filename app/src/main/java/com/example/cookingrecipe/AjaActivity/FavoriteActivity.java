package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingrecipe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import AjaAdapter.FavoriteAdapter;
import AjaAdapter.PostAdapter;
import AjaClassGetSet.Post;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private PostAdapter postAdapter;
    private FavoriteAdapter favoriteAdapter;
    private List<Post> favoriteList;
    private List<Post> postList;

    private DatabaseReference favoriteRef, postRef, favoritesRef, postsRef;
    private FirebaseUser currentUser;
    private String currentUserUID;

    private Drawable likeIconDefault;
    private Drawable likeIconFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerView_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        favoriteList = new ArrayList<>();
        Intent intent = getIntent();
        List<Post> favoriteList = intent.getParcelableArrayListExtra("favoriteList");
        favoriteAdapter = new FavoriteAdapter(favoriteList, this, true);
        recyclerView.setAdapter(favoriteAdapter);

        // Inisialisasi drawable ikon
        likeIconDefault = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_nav);
        likeIconFavorite = ContextCompat.getDrawable(this, R.drawable.ic_bookmark);

        // Dapatkan referensi ke daftar favorit pengguna saat ini
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favoritesRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserUID).child("like");

        // Mendapatkan referensi database
        DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("like");

        // Membuat query untuk mengambil daftar post yang telah di-like oleh pengguna
        Query favoriteQuery = favoriteRef.orderByValue().equalTo(true);

        // Mendapatkan data dari database
        favoriteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> likedPostIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postId = snapshot.getKey();
                    likedPostIds.add(postId);
                }

                // Memuat daftar post yang telah di-like dari database
                loadFavoritePosts(recyclerView, likedPostIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(FavoriteActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        postAdapter = new PostAdapter(FavoriteActivity.this, new ArrayList<>());
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                // Menampilkan deskripsi lengkap post yang diklik
                Intent intent = new Intent(FavoriteActivity.this, PostDetailActivity.class);
                intent.putExtra("post", post);
                intent.putExtra("id_post", post.getIdPost());
                intent.putExtra("id_creator", post.getIdCreator());
                intent.putExtra("id_subcategory", post.getIdSubcategory());
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(Post post) {
                toggleFavorite(post);
                Intent intent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                intent.putParcelableArrayListExtra("postList", (ArrayList<? extends Parcelable>) postList);
                startActivity(intent);
                finish();            }

            @Override
            public void onRefreshRequested() {
                Intent intent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                startActivity(intent);
                finish();
            }

        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MainmainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadFavoritePosts(RecyclerView recyclerView, List<String> likedPostIds) {
        postRef = FirebaseDatabase.getInstance().getReference("post");
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> favoritePosts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (likedPostIds.contains(post.getIdPost())) {
                        favoritePosts.add(post);
                    }
                }

                // Inisialisasi dan mengatur adapter untuk RecyclerView
                PostAdapter favoriteAdapter = new PostAdapter(FavoriteActivity.this, favoritePosts);
                recyclerView.setAdapter(favoriteAdapter);

                // Mengatur listener untuk menangani klik pada item post
                favoriteAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Post post) {
                        // Menampilkan deskripsi lengkap post yang diklik
                        Intent intent = new Intent(FavoriteActivity.this, PostDetailActivity.class);
                        intent.putExtra("post", post);
                        intent.putExtra("id_post", post.getIdPost());
                        intent.putExtra("id_creator", post.getIdCreator());
                        intent.putExtra("id_subcategory", post.getIdSubcategory());
                        startActivity(intent);
                    }

                    @Override
                    public void onFavoriteClick(Post post) {
                        toggleFavorite(post);
                        Intent intent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        intent.putParcelableArrayListExtra("postList", (ArrayList<? extends Parcelable>) postList);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onRefreshRequested() {
                        Intent intent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kesalahan yang terjadi saat mengambil data dari database
                Toast.makeText(FavoriteActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Membersihkan semua history aktivitas
        finishAffinity();
        super.onBackPressed();
    }

    private void toggleFavorite(Post post) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("like").child(post.getIdPost());

            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Post sudah ada di daftar favorit, hapus dari daftar favorit
                        favoriteRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FavoriteActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FavoriteActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Post belum ada di daftar favorit, tambahkan ke daftar favorit
                        favoriteRef.setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FavoriteActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FavoriteActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
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