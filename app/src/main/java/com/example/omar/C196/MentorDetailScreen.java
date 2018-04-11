package com.example.omar.C196;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_3;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_4;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_5;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_6;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_8;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_1;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_2;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class MentorDetailScreen extends AppCompatActivity {

    DatabaseHelper db;
    String mentorId, mentorName, mentorPhone, mentorEmail, mentorAddress;
    TextView tv_MentorName, tv_MentorPhone, tv_MentorEmail, tv_MentorAddress;


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_detail_screen);

        tv_MentorName = findViewById(R.id.tv_MentorName);
        tv_MentorPhone = findViewById(R.id.tv_MentorPhone);
        tv_MentorEmail = findViewById(R.id.tv_MentorEmail);
        tv_MentorAddress = findViewById(R.id.tv_MentorAddress);

        Intent intent = getIntent();
        mentorId = intent.getStringExtra("mentorId");
        mentorName = intent.getStringExtra("mentorName");
        mentorPhone = intent.getStringExtra("mentorPhone");
        mentorEmail = intent.getStringExtra("mentorEmail");
        mentorAddress = intent.getStringExtra("mentorAddress");

        tv_MentorName.setText(mentorName);
        tv_MentorPhone.setText(mentorPhone);
        tv_MentorEmail.setText(mentorEmail);
        tv_MentorAddress.setText(mentorAddress);
    }

    public void editMentor(View view){
        db =  new DatabaseHelper(this, null, null, 1);
        Intent intent = new Intent(MentorDetailScreen.this, MentorEditScreen.class);
        intent.putExtra("mentorId", mentorId);
        intent.putExtra("mentorName", mentorName);
        intent.putExtra("mentorEmail", mentorEmail);
        intent.putExtra("mentorPhone", mentorPhone);
        intent.putExtra("mentorAddress", mentorAddress);
        startActivity(intent);
    }

    public void deleteMentor(View view){
        db =  new DatabaseHelper(this, null, null, 1);
        String sql = "DELETE FROM " + MENTOR_TABLE + " WHERE " + COL_MENTOR_1 + " = " + mentorId;
        db.getWritableDatabase().execSQL(sql);
        Intent intent = new Intent(MentorDetailScreen.this, MentorScreen.class);
        startActivity(intent);
    }
}

