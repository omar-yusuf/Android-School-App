package com.example.omar.C196;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.ASSESSMENT_TABLE;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_2;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_7;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class AssessmentDetailScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_EditAssessment, tv_DeleteAssessment, tv_CourseName, tv_AssessmentName, tv_AssessmentType, tv_GoalDate, tv_DueDate;
    String courseName, assessmentName, assessmentType, goalDate, dueDate = "";
    CheckBox cb_GoalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_detail_screen);
        tv_EditAssessment = findViewById(R.id.tv_EditAssessment);
        tv_DeleteAssessment = findViewById(R.id.tv_DeleteAssessment);
        tv_CourseName = findViewById(R.id.tv_CourseName);
        tv_AssessmentName = findViewById(R.id.tv_AssessmentName);
        tv_AssessmentType = findViewById(R.id.tv_AssessmentType);
        tv_GoalDate = findViewById(R.id.tv_GoalDate);
        tv_DueDate = findViewById(R.id.tv_DueDate);
        cb_GoalDate = findViewById(R.id.cb_GoalDate);

        Intent intent = getIntent();
        assessmentName = intent.getStringExtra("courseName").trim();

        db = new DatabaseHelper(this, null, null, 1);
        String sql = "SELECT *" +
                " FROM " + ASSESSMENT_TABLE +
                " WHERE " + COL_ASSESSMENT_2 + " = \"" + assessmentName + "\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            courseName = cursor.getString(cursor.getColumnIndex("course"));
            assessmentType = cursor.getString(cursor.getColumnIndex("type"));
            goalDate = cursor.getString(cursor.getColumnIndex("goaldate"));
            dueDate = cursor.getString(cursor.getColumnIndex(COL_ASSESSMENT_7));
            if(cursor.getString(cursor.getColumnIndex("notify")).contains("y")){
                cb_GoalDate.setChecked(true);
            }
        }
        tv_EditAssessment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(AssessmentDetailScreen.this, AssessmentEditScreen.class);
               intent.putExtra("courseName", courseName);
               intent.putExtra("assessmentName", assessmentName);
               intent.putExtra("assessmentType", assessmentType);
               startActivity(intent);
           }
       });
        tv_DeleteAssessment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String sql =  "DELETE FROM " + ASSESSMENT_TABLE +
                        " WHERE " + COL_ASSESSMENT_2 + " = \"" + assessmentName + "\"";
                 db.getWritableDatabase().execSQL(sql);
                 Intent intent = new Intent(AssessmentDetailScreen.this, AssessmentScreen.class);
                 startActivity(intent);
             }
         });

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


        tv_CourseName.setText(courseName);
        tv_AssessmentName.setText(assessmentName);
        tv_AssessmentType.setText(assessmentType);
        tv_GoalDate.setText(goalDate);
        tv_DueDate.setText(dueDate);
    }
}
