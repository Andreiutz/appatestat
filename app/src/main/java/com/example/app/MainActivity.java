package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);//merge sau sal?
        }

    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
            
    }

    public void onClickSignIn(View view) {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }


}