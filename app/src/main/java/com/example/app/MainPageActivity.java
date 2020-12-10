package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainPageActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    public void startQuiz(View view) {
        Intent intent = new Intent(MainPageActivity.this, QuizActivity.class);
        startActivity(intent);
    }

    public void logOutFunction(View view) {             /**Log Out button*/
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainPageActivity.this, SignInActivity.class);
        finish();
        startActivity(intent);
    }


}