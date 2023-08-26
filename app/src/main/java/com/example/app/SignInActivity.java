package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextTextPersonName5);
        password = findViewById(R.id.editTextTextPassword);
        signUpTextView = findViewById(R.id.signUpTextView);
    }

    public void updateUI(FirebaseUser user) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void onClickSignIn(View view) {

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();


        if (verifyEmailAdress(email) && verifyPassword(password)) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                                Map<String, Object> map = new HashMap<>();

                                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());

                                map.put("password", password);

                                docRef.update(map);


                                Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                                finish();
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }


    public boolean verifyEmailAdress(String email) {
        if (email.isEmpty()) {
            this.email.setError("Email is required");
            this.email.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Please enter a valid email");
            this.email.requestFocus();
            return false;
        }
        return true;
    }

    public boolean verifyPassword(String password) {
        if (password.isEmpty()) {
            this.password.setError("Password required");
            this.password.requestFocus();
            return false;
        }
        return true;
    }

    public void onClickSignUp(View view)
    {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        finish();
        startActivity(intent);
    }


}