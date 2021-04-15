package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StatsActivity extends AppCompatActivity {


    private TextView percentage;
    private TextView name;
    private TextView email;
    private TextView nrTest;

    private static final String TAG = "StatsActivity";

    private static final String SCORE_PERCENTAGE = "score";
    private static final String NUMBER_OF_QUIZES = "number of quizes";
    private static final String USER_NAME = "name";
    private static final String EMAIL_INPUT = "email";

    private FirebaseFirestore mFire;
    private FirebaseAuth mAuth;

    String userNameString;
    String quizString;
    String emailString;
    String scoreString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        percentage = findViewById(R.id.percentage);
        name = findViewById(R.id.numeId);
        email = findViewById(R.id.emailId);
        nrTest = findViewById(R.id.nrTesteId);

        mFire = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        setData();

    }

    private void setData() {
        FirebaseUser user = mAuth.getCurrentUser();



        DocumentReference docRef = mFire.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Toast.makeText(StatsActivity.this, "gaseste", Toast.LENGTH_SHORT).show();

                        emailString = document.get(EMAIL_INPUT).toString();
                        userNameString = document.get(USER_NAME).toString();
                        quizString = document.get(NUMBER_OF_QUIZES).toString();
                        scoreString = document.get(SCORE_PERCENTAGE).toString();



                        percentage.setText(String.format("%s%%", scoreString));
                        name.setText(String.format("Name: %s", userNameString));
                        email.setText(String.format("Email: %s", emailString));
                        nrTest.setText(String.format("No. quizes: %s", quizString));


                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}