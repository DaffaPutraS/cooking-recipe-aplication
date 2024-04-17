package com.example.cookingrecipe.AjaActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingrecipe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView txtBackLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reset);

        emailEditText = findViewById(R.id.edt_email_emailreset);
        resetPasswordButton = findViewById(R.id.btn_sendemail_emailreset);
        txtBackLogin = findViewById(R.id.txt_backlogin_emailreset);
        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        txtBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailResetActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password")
                .setMessage("Please check email message and input your new password there, if there is no message you can check spam section :). Then please input all your data in the next page with your new password that have been input by you earlier, thanks!")
                .setPositiveButton("Lets Go!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetPassword();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailResetActivity.this, "Reset password email has been sent", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmailResetActivity.this, ForgotPassActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EmailResetActivity.this, "Failed to send reset password email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}