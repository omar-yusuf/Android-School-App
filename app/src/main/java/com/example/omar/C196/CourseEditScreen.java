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
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_2;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_4;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_5;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class CourseEditScreen extends AppCompatActivity {

    DatabaseHelper db;
    EditText et_Notes;
    String mentorName, mentorPhone, mentorEmail, mentorId, courseName, startDate, endDate, status, notes, notify, oCourseName, oMentorName = "";
    EditText et_MentorName, et_MentorPhone, et_MentorEmail, et_CourseTitle, et_StartDate, et_EndDate, et_Status;
    CheckBox cb_AssessmentAlert;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_edit_screen);

        et_Status = findViewById(R.id.et_Status);
        et_EndDate = findViewById(R.id.et_EndDate);
        et_StartDate = findViewById(R.id.et_StartDate);
        et_MentorName = findViewById(R.id.et_MentorName);
        et_MentorPhone = findViewById(R.id.et_MentorPhone);
        et_MentorEmail = findViewById(R.id.et_MentorEmail);
        et_CourseTitle = findViewById(R.id.et_CourseTitle);
        et_Notes = findViewById(R.id.et_Notes);
        cb_AssessmentAlert = findViewById(R.id.cb_AssessmentAlert);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        status = intent.getStringExtra("status");
        mentorName = intent.getStringExtra("mentorName");
        mentorPhone = intent.getStringExtra("mentorPhone");
        mentorEmail = intent.getStringExtra("mentorEmail");
        notes = intent.getStringExtra("notes");
        notify = intent.getStringExtra("notify");

        et_CourseTitle.setText(courseName);
        et_MentorName.setText(mentorName);
        et_MentorPhone.setText(mentorPhone);
        et_MentorEmail.setText(mentorEmail);
        et_StartDate.setText(startDate);
        et_EndDate.setText(endDate);
        et_Status.setText(status);
        et_Notes.setText(notes);
        if(notify.contains("yes")) {
            cb_AssessmentAlert.setChecked(true);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_9 + " = \"yes\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql,null);
        while(cursor.moveToNext()){
            Calendar startDate = new GregorianCalendar();
            Calendar endDate = new GregorianCalendar();
            startDate.setTime(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("startDate")))));
            endDate.setTime(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("endDate")))));
            if(Calendar.getInstance().getTime().after(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("startDate")))))
                    && Calendar.getInstance().getTime().before(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("endDate")))))){
                upcomingAlerts.add("COURSE DATE: " + cursor.getString(cursor.getColumnIndex(COL_COURSE_2)));
            }
        }
    }

    public void btn_EditCouse(View view){
        db =  new DatabaseHelper(this, null, null, 1);
        oCourseName = courseName;
        oMentorName = mentorName;
        courseName = et_CourseTitle.getText().toString();
        startDate = et_StartDate.getText().toString();
        endDate = et_EndDate.getText().toString();
        status = et_Status.getText().toString();
        mentorName = et_MentorName.getText().toString();
        mentorEmail = et_MentorEmail.getText().toString();
        mentorPhone = et_MentorPhone.getText().toString();
        notes = et_Notes.getText().toString();
        notify = "no";
        if(cb_AssessmentAlert.isChecked()){
            notify = "yes";
        }
        String sql = " " +
                " UPDATE " + COURSE_TABLE +
                " SET " + COL_COURSE_2 + " = \"" + courseName + "\", " +
                COL_COURSE_3 + " = \"" + startDate + "\", " +
                COL_COURSE_4 + " = \"" + endDate + "\", " +
                COL_COURSE_5 + " = \"" + mentorName+ "\", " +
                COL_COURSE_6 + " = \"" + status + "\", " +
                COL_COURSE_8 + " = \"" + notes + "\", " +
                COL_COURSE_9 + " = \"" + notify + "\"" +
                " WHERE " + COL_COURSE_2 + " = \"" + oCourseName + "\"";
        db.getWritableDatabase().execSQL(sql);
        sql = "" +
                " UPDATE " + MENTOR_TABLE +
                " SET " + COL_MENTOR_2 + " = \"" + mentorName + "\"," +
                COL_MENTOR_4 + " = \"" + mentorPhone + "\", " +
                COL_MENTOR_5 + " = \"" + mentorEmail + "\"" +
                " WHERE " + COL_MENTOR_2 + " = \"" + oMentorName + "\"";
        db.getWritableDatabase().execSQL(sql);
        Log.d("MUH TAAAAAAAAAAAAG", sql);
        Intent intent = new Intent(CourseEditScreen.this, CourseDetailScreen.class);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
    }

    public void btn_DeleteCourse(View view){

    }
}

