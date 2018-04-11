package com.example.omar.C196;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.ASSESSMENT_TABLE;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_2;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_3;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_4;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_5;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_6;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_1;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_2;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_3;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_4;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_5;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class MentorEditScreen extends AppCompatActivity {

    DatabaseHelper db;
    EditText et_MentorName, et_MentorEmail, et_MentorPhone, et_MentorAddress;
    String mentorName, mentorEmail, mentorPhone, mentorAddress, mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_edit_screen);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseHelper(this, null, null, 1);

        Intent intent = getIntent();
        mentorId = intent.getStringExtra("mentorId");
        mentorName = intent.getStringExtra("mentorName");
        mentorEmail = intent.getStringExtra("mentorEmail");
        mentorPhone = intent.getStringExtra("mentorPhone");
        mentorAddress = intent.getStringExtra("mentorAddress");

        et_MentorAddress = findViewById(R.id.et_MentorAddress);
        et_MentorEmail = findViewById(R.id.et_MentorEmail);
        et_MentorPhone = findViewById(R.id.et_MentorPhone);
        et_MentorName = findViewById(R.id.et_MentorName);

        et_MentorName.setText(mentorName);
        et_MentorEmail.setText(mentorEmail);
        et_MentorPhone.setText(mentorPhone);
        et_MentorAddress.setText(mentorAddress);
    }

    public void btn_EditAssessment(View view){
        mentorName = et_MentorName.getText().toString();
        mentorEmail = et_MentorEmail.getText().toString();
        mentorPhone = et_MentorPhone.getText().toString();
        mentorAddress = et_MentorAddress.getText().toString();
        String sql = "" +
                " UPDATE " + MENTOR_TABLE +
                " SET " + COL_MENTOR_2 + " = \"" + mentorName + "\"," +
                COL_MENTOR_3 + " = \"" + mentorAddress + "\"," +
                COL_MENTOR_4 + " = \"" + mentorPhone + "\"," +
                COL_MENTOR_5 + " = \"" + mentorEmail + "\"" +
                " WHERE " + COL_MENTOR_1 + " = \"" + mentorId + "\"";
        db.getWritableDatabase().execSQL(sql);
        Intent intent = new Intent(MentorEditScreen.this, MentorDetailScreen.class);
        intent.putExtra("mentorId", mentorId);
        intent.putExtra("mentorName", mentorName);
        intent.putExtra("mentorEmail", mentorEmail);
        intent.putExtra("mentorPhone", mentorPhone);
        intent.putExtra("mentorAddress", mentorAddress);
        startActivity(intent);
    }
}
