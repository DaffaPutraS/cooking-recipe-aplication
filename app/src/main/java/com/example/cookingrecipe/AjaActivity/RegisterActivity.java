package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingrecipe.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import AjaClassGetSet.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private Button registerButton;
    private TextView txtLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi komponen tampilan
        etUsername = findViewById(R.id.edt_name_register);
        etEmail = findViewById(R.id.edt_email_register);
        etPassword = findViewById(R.id.edt_password_register);
        registerButton = findViewById(R.id.btn_register_register);
        txtLogin = findViewById(R.id.txt_login_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Proses registrasi pengguna
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registrasi berhasil, tambahkan data pengguna ke database
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            String name = etUsername.getText().toString().trim();
                            String email = etEmail.getText().toString().trim();
                            String password = etPassword.getText().toString().trim();

                            // Simpan data pengguna ke Realtime Database
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            User newUser = new User(name, email, password);
                            databaseRef.setValue(newUser);

                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Registrasi gagal, tampilkan pesan kesalahan
                            Toast.makeText(RegisterActivity.this, "Registrasi Gagal.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Menambahkan listener pada teks login
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
