package com.example.omar.C196;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import static com.example.omar.C196.DatabaseHelper.COL_TERM_4;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;
import static com.example.omar.C196.DatabaseHelper.TERM_TABLE;

public class NewCourseScreen extends AppCompatActivity {

    EditText et_CourseName, et_Notes;
    ArrayList<String> mentorList = new ArrayList<String>();
    ArrayList<String> termList = new ArrayList<String>();
    Spinner sp_StartMonth, sp_StartDay, sp_StartYear, sp_EndMonth, sp_EndDay,  sp_EndYear, sp_Status, sp_Mentor, sp_Term;
    String startMonth, startDay, startYear, endMonth, endDay, endYear, status, mentor, courseName, term, notes;
    CheckBox cb_AssessmentAlert;

    DatabaseHelper db;
    String notify = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_course_screen);
        db =  new DatabaseHelper(this, null, null, 1);

        et_CourseName = findViewById(R.id.et_CourseName);
        et_Notes = findViewById(R.id.et_Notes);
        sp_StartMonth = findViewById(R.id.sp_StartMonth);
        sp_StartDay = findViewById(R.id.sp_StartDay);
        sp_StartYear = findViewById(R.id.sp_StartYear);
        sp_EndMonth = findViewById(R.id.sp_EndMonth);
        sp_EndDay = findViewById(R.id.sp_EndDay);
        sp_EndYear = findViewById(R.id.sp_EndYear);
        sp_Status = findViewById(R.id.sp_Status);
        sp_Mentor = findViewById(R.id.sp_Mentor);
        sp_Term = findViewById(R.id.sp_Term);
        cb_AssessmentAlert = findViewById(R.id.cb_AssessmentAlert);

        Cursor cursor = db.getTableData(MENTOR_TABLE);
        while(cursor.moveToNext()){
            String newName = cursor.getString(cursor.getColumnIndex("name"));
            mentorList.add(newName);
        }
        cursor = db.getTableData(TERM_TABLE);
        while(cursor.moveToNext()){
            String newName = cursor.getString(cursor.getColumnIndex(COL_TERM_4));
            termList.add(newName);
        }

        ArrayAdapter<CharSequence> startMonthAdapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        startMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_StartMonth = findViewById(R.id.sp_StartMonth);
        sp_StartMonth.setAdapter(startMonthAdapter);

        ArrayAdapter<CharSequence> startDayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        startDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_StartDay = findViewById(R.id.sp_StartDay);
        sp_StartDay.setAdapter(startDayAdapter);

        ArrayAdapter<CharSequence> startYearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        startYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_StartYear = findViewById(R.id.sp_StartYear);
        sp_StartYear.setAdapter(startYearAdapter);

        ArrayAdapter<CharSequence> endMonthAdapter = ArrayAdapter.createFromResource(this,
               R.array.months, android.R.layout.simple_spinner_item);
        endMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_EndMonth = findViewById(R.id.sp_EndMonth);
        sp_EndMonth.setAdapter(endMonthAdapter);

        ArrayAdapter<CharSequence> endDayAdapter = ArrayAdapter.createFromResource(this,
               R.array.days, android.R.layout.simple_spinner_item);
        endDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_EndDay = findViewById(R.id.sp_EndDay);
        sp_EndDay.setAdapter(endDayAdapter);

        ArrayAdapter<CharSequence> endYearAdapter = ArrayAdapter.createFromResource(this,
               R.array.years, android.R.layout.simple_spinner_item);
        endYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_EndYear = findViewById(R.id. sp_EndYear);
        sp_EndYear.setAdapter(endYearAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Status = findViewById(R.id.sp_Status);
        sp_Status.setAdapter(statusAdapter);

        sp_Status.setAdapter(statusAdapter);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Status = findViewById(R.id.sp_Status);
        sp_Status.setAdapter(statusAdapter);

        ArrayAdapter<String> mentorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mentorList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Mentor.setAdapter(mentorAdapter);

        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,termList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Term.setAdapter(termAdapter);

        if(termList.isEmpty()){
            sp_Term.setActivated(false);
        }

    }

    public void btn_NewCourse(View view){
        courseName = et_CourseName.getText().toString();
        notes = et_Notes.getText().toString();
        startMonth = sp_StartMonth.getSelectedItem().toString();
        startDay = sp_StartDay.getSelectedItem().toString();
        startYear = sp_StartYear.getSelectedItem().toString();
        endMonth = sp_EndMonth.getSelectedItem().toString();
        endDay = sp_EndDay.getSelectedItem().toString();
        endYear = sp_EndYear.getSelectedItem().toString();
        status = sp_Status.getSelectedItem().toString();
        mentor = sp_Mentor.getSelectedItem().toString();
        if(!termList.isEmpty()) {
            term = sp_Term.getSelectedItem().toString().replaceAll("[^\\d.]", "");;
        }
        String startDate = startMonth + " " + startDay + ", " + startYear;
        String endDate = endMonth + " " + endDay + ", " + endYear;
        if(cb_AssessmentAlert.isChecked()){
            notify = "yes";
        }
        db.insertCourse(courseName, startDate, endDate, mentor, status, term, notes, notify);
        Intent intent = new Intent(NewCourseScreen.this, CourseScreen.class);
        startActivity(intent);
    }
}
