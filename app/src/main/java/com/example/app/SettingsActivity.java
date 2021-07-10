package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

    }


    public void logOutFunction(View view) {             /**Log Out button*/
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
        finish();
        startActivity(intent);
    }

    public void changePassword(View view) {
        AlertDialog alerdDialog = new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Change password?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Check email", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    Intent intent = new Intent(SettingsActivity.this, MainPageActivity.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void stats(View view) {
        Intent intent = new Intent(SettingsActivity.this, StatsActivity.class);
        startActivity(intent);
    }

}