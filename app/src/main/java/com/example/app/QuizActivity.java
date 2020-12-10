package com.example.app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 900000;

    private static final String RIGHT_ANSWER_KEY = "right_answer";
    private static final String WRONG_ANSWER_KEY1 = "wrong_answer1";
    private static final String WRONG_ANSWER_KEY2 = "wrong_answer2";
    private static final String WRONG_ANSWER_KEY3 = "wrong_answer3";

    private static final String QUESTION_KEY = "question";

    private static final String TAG = "QuizActivity";

    private TextView textView;

    private TextView questionNumber;
    private TextView correctAnswers;
    private TextView wrongAnswers;

    private CountDownTimer countDownTimer;

    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    private RadioButton answer1;

    private RadioButton answer2;

    private RadioButton answer3;

    private RadioButton answer4;

    private TextView question;
    private int questionId;
    String questionText;

    private Button  checkAnswer;
    String rightAnswerText;
    String wrongAnswerText1;
    String wrongAnswerText2;
    String wrongAnswerText3;


   ArrayList<Integer> checkedList = new ArrayList<Integer>();

    private ArrayList<String> answers = new ArrayList<String>();

    private FirebaseFirestore mFire;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textView = findViewById(R.id.countdown_text);               /**Declarare variabile*/

        answer1 = findViewById(R.id.answer1);

        answer2 = findViewById(R.id.answer2);

        answer3 = findViewById(R.id.answer3);

        answer4 = findViewById(R.id.answer4);

        question = findViewById(R.id.question);

        checkAnswer = findViewById(R.id.checkAnswer_btn);

        questionNumber = findViewById(R.id.numberQuestions);
        questionNumber.setText("1/15");

        correctAnswers = findViewById(R.id.corecte);
        correctAnswers.setText("0");

        wrongAnswers = findViewById(R.id.gresite);
        wrongAnswers.setText("0");

        mFire = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        questionId = 1;


        startTimer();
        updateQuestion();



    }

    private void startTimer() {                                                         /**Creeaza timer-ul de pe ecran*/
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }
            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "finish", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;

        if (seconds > 9) {
            timeLeftFormatted = minutes + ":"+seconds;
        } else {
            timeLeftFormatted = minutes + ":0" + seconds;
        }

        textView.setText(timeLeftFormatted);
    }
                                                        /**************************************                      Se ocupa de cele 4 variante de raspuns*/
    public void onClick1(View view) {
        if (answer1.isSelected()) {
                answer1.setSelected(false);
                answer1.setChecked(false);
        } else {
            unCheckOtherRadioButtons(answer1);
        }
    }

    public void onClick2(View view) {
        if (answer2.isSelected()) {
            answer2.setSelected(false);
            answer2.setChecked(false);
        } else {
            unCheckOtherRadioButtons(answer2);
        }
    }

    public void onClick3(View view) {
        if (answer3.isSelected()) {
            answer3.setSelected(false);
            answer3.setChecked(false);
        } else {
            unCheckOtherRadioButtons(answer3);
        }
    }

    public void onClick4(View view) {
        if (answer4.isSelected()) {
            answer4.setSelected(false);
            answer4.setChecked(false);
        } else {
            unCheckOtherRadioButtons(answer4);
        }
    }

    void unCheckOtherRadioButtons(RadioButton rb) {
        answer1.setSelected(false);
        answer1.setChecked(false);
        answer2.setSelected(false);
        answer2.setChecked(false);
        answer3.setSelected(false);
        answer3.setChecked(false);
        answer4.setSelected(false);
        answer4.setChecked(false);

        rb.setSelected(true);
        rb.setChecked(true);

    }
                                                        /********************************************************/

    private void uncheckAllRadioButtons() {
        answer1.setSelected(false);
        answer1.setChecked(false);
        answer2.setSelected(false);
        answer2.setChecked(false);
        answer3.setSelected(false);
        answer3.setChecked(false);
        answer4.setSelected(false);
        answer4.setChecked(false);
    }


    private int getRandomValue() {
        Random random = new Random();
        int x = random.nextInt(5);
        return x+1;
    }


    RadioButton sameText() {
        if (answer1.isChecked()) return answer1;
        else if (answer2.isChecked()) return answer2;
        else if (answer3.isChecked()) return answer3;
        else if (answer4.isChecked()) return answer4;
        else return null;
    }




    private void updateQuestionNumber()
    {

    }

    private void fireStoreScoreUpdate()                                                                                         //Actualizeaza scorul retinut in firestore (ar trebui apelata la finalul testului)
    {
        FirebaseUser user = mAuth.getCurrentUser();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());

        Map<String, Object> map = new HashMap<>();

        map.put("score", 1);

        docRef.update(map);

    }

    private void updateNumberQuestionView(boolean answer)                                //creste contorul de intrebari corecte/gresite in functie
    {                                                                                                         ///de raspunsul dat


        int questionNr;

        if (answer)
        {
            questionNr = Integer.parseInt(correctAnswers.getText().toString());
            questionNr++;

            String newText = Integer.toString(questionNr);

            correctAnswers.setText(newText);
        }
        else
        {
            questionNr = Integer.parseInt(wrongAnswers.getText().toString());
            questionNr++;

            String newText = Integer.toString(questionNr);

            wrongAnswers.setText(newText);
        }

        String nq = questionId+"/15";  ///id-ul intrebarii noi

        questionNumber.setText(nq);

    }


    private void changeQuestion(boolean answer)
    {
        questionId++;

        if (questionId < 16) {

            uncheckAllRadioButtons();

            updateNumberQuestionView(answer);

            updateQuestion();
        }
        else                                            ///s-a raspuns la toate intrebarile
        {
            //stopQuiz()
        }

    }



    public void checkAnswer(View view) {
        /**
         * Verifica raspunsul
         * Adauga scor
         */

        RadioButton check = sameText();

        fireStoreScoreUpdate();




        if (check != null) {

            boolean goodAnswer = check.getText().equals(rightAnswerText);

            if (goodAnswer) {
                Toast.makeText(this, "Corect!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Gresit!", Toast.LENGTH_SHORT).show();

            changeQuestion(goodAnswer);

        } else {
            Toast.makeText(this, "Alegeti un raspuns!", Toast.LENGTH_SHORT).show();
        }



    }


    private void updateQuestion() {

        int newIdQuestion = getRandomValue();

        //while (checkedList.contains(newIdQuestion)) newIdQuestion = getRandomValue();

        //checkedList.add(newIdQuestion);


        DocumentReference docRef = mFire.collection("question").document(Integer.toString(newIdQuestion));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        questionText = document.getString(QUESTION_KEY);
                        rightAnswerText = document.getString(RIGHT_ANSWER_KEY);
                        wrongAnswerText1 = document.getString(WRONG_ANSWER_KEY1);
                        wrongAnswerText2 = document.getString(WRONG_ANSWER_KEY2);
                        wrongAnswerText3 = document.getString(WRONG_ANSWER_KEY3);
                        question.setText(questionText);

                        answers.add(rightAnswerText);
                        answers.add(wrongAnswerText1);
                        answers.add(wrongAnswerText2);
                        answers.add(wrongAnswerText3);

                        Collections.shuffle(answers);

                        answer1.setText(answers.get(0));
                        answer2.setText(answers.get(1));
                        answer3.setText(answers.get(2));
                        answer4.setText(answers.get(3));

                        answers.clear();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }



}