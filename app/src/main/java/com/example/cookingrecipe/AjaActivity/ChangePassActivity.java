package com.example.cookingrecipe.AjaActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingrecipe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePassActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditTextCurrent, passwordEditText;
    private Button resetPasswordButton, cancelButton;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        emailEditText = findViewById(R.id.edt_email_changepass);
        passwordEditTextCurrent = findViewById(R.id.edt_currentpass_changepass);
        passwordEditText = findViewById(R.id.edt_newpass_changepass);
        resetPasswordButton = findViewById(R.id.btn_changepass_changepass);
        cancelButton = findViewById(R.id.btn_cancel_changepass);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Panggil metode untuk mengisi field email dengan email pengguna yang sedang login
        populateEmailField();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateEmailField() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            emailEditText.setText(email);
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password").setMessage("Are you sure you want to reset your password?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetPassword();
            }
        }).setNegativeButton("No", null).show();
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();
        String currentPassword = passwordEditTextCurrent.getText().toString().trim();
        Log.d("ResetPassword", "Email: " + email);
        Log.d("ResetPassword", "Current Password: " + currentPassword);
        Log.d("ResetPassword", "New Password: " + newPassword);

        if (email.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your email and new password", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (!currentPassword.isEmpty()) {
                if (!currentPassword.equals(newPassword)) {
                    // Lakukan tindakan penggantian password
                    AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

                    // Reautentikasi pengguna dengan credential saat ini sebelum mengubah password
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Lakukan penggantian password di Firebase Authentication
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Lakukan penggantian password di Firebase Realtime Database
                                            updatePasswordInRealtimeDatabase(email, newPassword);
                                            Intent intent = new Intent(ChangePassActivity.this, MainmainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ChangePassActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d("ResetPassword", "Authentication failed: " + task.getException().getMessage());
                                Toast.makeText(ChangePassActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Current password and new password cannot be the same", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePasswordInRealtimeDatabase(String email, String newPassword) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    usersRef.child(userId).child("password").setValue(newPassword);
                }
                Toast.makeText(ChangePassActivity.this, "Password has been reset successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassActivity.this, MainmainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangePassActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
