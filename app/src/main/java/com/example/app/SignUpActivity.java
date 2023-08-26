package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText confirmPassword;


    String defaultScore = "0";
    String numberOfQuizes = "0";

    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPersonName3);
        confirmPassword = findViewById(R.id.editTextTextPersonName4);
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * @param name
     * @param emailAdress
     * @param uid         adds a new user to the firestore
     */

    private void addUser(String name, String emailAdress, String uid, String password, String score, String numberOfQuizes) {

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", emailAdress);
        user.put("password", password);
        user.put("score", score);
        user.put("number of quizes", numberOfQuizes);

        // Add a new document with a generated ID
        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(SignUpActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * @param view sign up function
     */

    public void signUp(View view) {

        String pass = password.getText().toString();
        String confPass = confirmPassword.getText().toString();
        String name = this.name.getText().toString();
        String emailAdress = email.getText().toString();

        if (validatepassword(pass, confPass) && verifyEmailAdress(emailAdress)) {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();

                                String uid = user.getUid();

                                addUser(name, emailAdress, uid, pass, defaultScore, numberOfQuizes);

                                Intent intent = new Intent(SignUpActivity.this, MainPageActivity.class);
                                finish();
                                startActivity(intent);

                            } else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignUpActivity.this, "You are already registered!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }


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

    public boolean validatepassword(String pass, String confPass) {
        if (!pass.contentEquals(confPass)) {
            this.password.setError("password did not match!");
            this.password.requestFocus();
            return false;
        }

        if (pass.isEmpty()) {
            this.password.setError("Password is required");
            this.password.requestFocus();
            return false;
        }

        if (pass.length() < 6) {
            this.password.setError("Minimum lenght of password should be 6");
            this.password.requestFocus();
            return false;
        }
        return true;

    }

}

