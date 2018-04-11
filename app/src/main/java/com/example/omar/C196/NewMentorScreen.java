package com.example.omar.C196;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class NewMentorScreen extends AppCompatActivity {

    EditText et_MentorName, et_MentorPhone, et_MentorEmail, et_MentorAddress;
    String mentorName, mentorPhone, mentorEmail, mentorAddress;

    DatabaseHelper db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mentor_screen);
        db =  new DatabaseHelper(this, null, null, 1);
    }

    public void btn_AddMentor(View view){
        et_MentorName = findViewById(R.id.et_MentorName);
        et_MentorAddress = findViewById(R.id.et_MentorAddress);
        et_MentorPhone = findViewById(R.id.et_MentorPhone);
        et_MentorEmail = findViewById(R.id.et_MentorEmail);

        mentorName = et_MentorName.getText().toString();
        mentorPhone = et_MentorPhone.getText().toString();
        mentorEmail = et_MentorEmail.getText().toString();
        mentorAddress = et_MentorAddress.getText().toString();
        db.insertMentor(mentorName, mentorPhone, mentorEmail, mentorAddress);
        Intent intent = new Intent(NewMentorScreen.this, MentorScreen.class);
        startActivity(intent);
    }
}
