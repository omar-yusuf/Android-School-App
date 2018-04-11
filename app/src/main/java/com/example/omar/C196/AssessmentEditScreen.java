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
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class AssessmentEditScreen extends AppCompatActivity {

    DatabaseHelper db;
    EditText et_CourseName, et_AssessmentName, et_AssessmentType, et_GoalDate;
    String courseName, assessmentName, assessmentType, oAssessmentName, goalDate, notify = "";
    CheckBox cb_GoalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_edit_screen);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseHelper(this, null, null, 1);

        Intent intent = getIntent();
        assessmentName = intent.getStringExtra("assessmentName");
        courseName = intent.getStringExtra("courseName");
        assessmentType = intent.getStringExtra("assessmentType");
        oAssessmentName = assessmentName;

        et_AssessmentName = findViewById(R.id.et_AssessmentName);
        et_CourseName = findViewById(R.id.et_CourseName);
        et_AssessmentType = findViewById(R.id.et_AssessmentType);
        et_GoalDate = findViewById(R.id.et_GoalDate);
        cb_GoalDate = findViewById(R.id.cb_GoalDate);

        String sql = "" +
                " SELECT *" +
                " FROM " + ASSESSMENT_TABLE +
                " WHERE " + COL_ASSESSMENT_2 + " = \"" + assessmentName + "\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            goalDate = cursor.getString(cursor.getColumnIndex(COL_ASSESSMENT_5));
            if (cursor.getString(cursor.getColumnIndex("notify")).contains("y")) {
                cb_GoalDate.setChecked(true);
            }
        }

        et_AssessmentName.setText(assessmentName);
        et_CourseName.setText(courseName);
        et_AssessmentType.setText(assessmentType);
        et_GoalDate.setText(goalDate);

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

    public void btn_EditAssessment(View view){
        if(cb_GoalDate.isChecked()){
            notify = "yes";
        } else {
            notify = "no";
        }

        assessmentName = et_AssessmentName.getText().toString();
        courseName = et_CourseName.getText().toString();
        assessmentType = et_AssessmentType.getText().toString();
        goalDate = et_GoalDate.getText().toString();
        String sql = "" +
                " UPDATE " + ASSESSMENT_TABLE +
                " SET " + COL_ASSESSMENT_2 + " = \"" + assessmentName + "\"," +
                COL_ASSESSMENT_3 + " = \"" + courseName + "\"," +
                COL_ASSESSMENT_4 + " = \"" + assessmentType + "\"," +
                COL_ASSESSMENT_5 + " = \"" + goalDate + "\"," +
                COL_ASSESSMENT_6 + " = \"" + notify + "\"" +
                " WHERE " + COL_ASSESSMENT_2 + " = \"" + oAssessmentName + "\"";
        db.getWritableDatabase().execSQL(sql);
        Intent intent = new Intent(AssessmentEditScreen.this, AssessmentScreen.class);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
    }
}
