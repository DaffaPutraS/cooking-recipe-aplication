package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingrecipe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassActivity extends AppCompatActivity {
    private EditText emailEditText, nameEditText, passwordEditText;
    private Button resetButton, loginButton;

    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        // Menginisialisasi elemen UI
        emailEditText = findViewById(R.id.edt_email_forgotpass);
        nameEditText = findViewById(R.id.edt_name_forgotpass);
        passwordEditText = findViewById(R.id.edt_password_forgotpass);
        resetButton = findViewById(R.id.btn_reset_forgotpass);
        loginButton = findViewById(R.id.btn_login_forgotpass);

        // Mengambil referensi Firebase Realtime Database
        adminsRef = FirebaseDatabase.getInstance().getReference("users");

        // Menggunakan event listener untuk mengupdate password saat tombol resetButton ditekan
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void resetForm() {
        String email = emailEditText.getText().toString().trim();
        String newName = nameEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        // Mengambil referensi pengguna berdasarkan email
        adminsRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        // Mengupdate nama dan password pengguna
                        adminsRef.child(userId).child("name").setValue(newName);
                        adminsRef.child(userId).child("password").setValue(newPassword);
                    }
                    alert("Password updated successfully!");
                    startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
                    finish();
                } else {
                    alert("Email not found!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle jika terjadi error saat mengakses database
                alert("Database error: " + databaseError.getMessage());
            }
        });
    }

    private void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
