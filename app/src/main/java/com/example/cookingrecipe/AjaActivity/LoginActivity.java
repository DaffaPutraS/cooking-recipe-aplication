package com.example.cookingrecipe.AjaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import AjaManager.SessionManager;

import com.example.cookingrecipe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    private TextView registerTxt, forgotTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseApp
        FirebaseApp.initializeApp(this);

        // Inisialisasi Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(LoginActivity.this);

        // Cek jika pengguna sudah login sebelumnya
        if (sessionManager.isLoggedIn()) {
            // Pengguna sudah login sebelumnya, arahkan ke halaman utama
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Inisialisasi komponen tampilan
        emailEditText = findViewById(R.id.edt_email_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        loginButton = findViewById(R.id.btn_login_login);

        forgotTxt = findViewById(R.id.txt_forgot_login);
        forgotTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arahkan pengguna ke RegisterActivity
                Intent intent = new Intent(LoginActivity.this, EmailResetActivity.class);
                startActivity(intent);
            }
        });

        registerTxt = findViewById(R.id.txt_register_login);
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arahkan pengguna ke RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Mohon lengkapi email dan password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Proses login pengguna
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login berhasil, arahkan ke halaman utama
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();

                                    // Set status login menjadi true menggunakan SessionManager
                                    sessionManager.setLoggedIn(true);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // Login gagal, tampilkan pesan kesalahan
                                    Toast.makeText(LoginActivity.this, "Login Gagal. Periksa email dan password Anda.", Toast.LENGTH_SHORT).show();
                                    if (task.getException() != null) {
                                        Log.e("LoginActivity", "Error: " + task.getException().getMessage());
                                    }

                                }
                            }
                        });
            }
        });
    }
}
