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
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_2;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class CourseDetailScreen extends AppCompatActivity {

    DatabaseHelper db;
    EditText et_Notes;
    String mentorName, mentorPhone, mentorEmail, mentorId, courseName, startDate, endDate, status, notes, notify, _id = "";
    TextView tv_MentorName, tv_MentorPhone, tv_MentorEmail, tv_CourseTitle, tv_StartDate, tv_EndDate, tv_Status, tv_Edit, tv_Delete;
    CheckBox cb_AssessmentAlert;


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_screen);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        db =  new DatabaseHelper(this, null, null, 1);
        db.getWritableDatabase();

        String sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_2 + " = \"" + courseName + "\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        cursor.moveToNext();
        Log.d("MUH TAAAAAAAAAAAAAG", sql);
        startDate = cursor.getString(cursor.getColumnIndex(COL_COURSE_3));
        endDate = cursor.getString(cursor.getColumnIndex(COL_COURSE_4));
        mentorId = cursor.getString(cursor.getColumnIndex(COL_COURSE_5));
        status = cursor.getString(cursor.getColumnIndex(COL_COURSE_6));
        notes = cursor.getString(cursor.getColumnIndex(COL_COURSE_8));
        notify = cursor.getString(cursor.getColumnIndex(COL_COURSE_9));
        sql = "SELECT * " + " FROM " + MENTOR_TABLE +  " WHERE " + COL_MENTOR_2 + "= \"" + mentorId + "\"";

        cursor = db.getWritableDatabase().rawQuery(sql, null);
        cursor.moveToNext();
        tv_Status = findViewById(R.id.tv_Status);
        tv_EndDate = findViewById(R.id.tv_EndDate);
        tv_StartDate = findViewById(R.id.tv_StartDate);
        tv_MentorName = findViewById(R.id.tv_MentorName);
        tv_MentorPhone = findViewById(R.id.tv_MentorPhone);
        tv_MentorEmail = findViewById(R.id.tv_MentorEmail);
        tv_CourseTitle = findViewById(R.id.tv_CourseTitle);
        tv_Edit = findViewById(R.id.tv_Edit);
        tv_Delete = findViewById(R.id.tv_Delete);
        et_Notes = findViewById(R.id.et_Notes);
        cb_AssessmentAlert = findViewById(R.id.cb_AssessmentAlert);

        Log.d("MUH TAAAAAAAAAAAAG", sql);
        mentorName = cursor.getString(cursor.getColumnIndex("name"));
        mentorPhone = cursor.getString(cursor.getColumnIndex("phone"));
        mentorEmail = cursor.getString(cursor.getColumnIndex("email"));
        tv_CourseTitle.setText(courseName);
        tv_MentorName.setText(mentorName);
        tv_MentorPhone.setText(mentorPhone);
        tv_MentorEmail.setText(mentorEmail);
        tv_StartDate.setText(startDate);
        tv_EndDate.setText(endDate);
        tv_Status.setText(status);
        et_Notes.setText(notes);
        if(notify.contains("yes")) {
            cb_AssessmentAlert.setChecked(true);
        }

        tv_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailScreen.this, CourseEditScreen.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                intent.putExtra("status", status);
                intent.putExtra("mentorName", mentorName);
                intent.putExtra("mentorPhone", mentorPhone);
                intent.putExtra("mentorEmail", mentorEmail);
                intent.putExtra("notes", notes);
                intent.putExtra("notify", notify);
                startActivity(intent);
            }
        });

        tv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql = "" +
                        " DELETE FROM " + COURSE_TABLE + "" +
                        " WHERE " + COL_COURSE_2 + " = \"" + courseName + "\"";
                db.getWritableDatabase().execSQL(sql);
                Intent intent = new Intent(CourseDetailScreen.this, CourseScreen.class);
                startActivity(intent);
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_9 + " = \"yes\"";
        cursor = db.getWritableDatabase().rawQuery(sql,null);
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
    public void btn_ViewAssessment(View view){
        Intent intent = new Intent(CourseDetailScreen.this, AssessmentScreen.class);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
    }

    public void btn_Share(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharedContent = notes;
        intent.putExtra(Intent.EXTRA_TEXT, sharedContent);
        startActivity(Intent.createChooser(intent, "Share using"));
    }
}

