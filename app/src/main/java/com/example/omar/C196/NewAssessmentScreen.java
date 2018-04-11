package com.example.omar.C196;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class NewAssessmentScreen extends AppCompatActivity {

    EditText et_CourseName, et_AssessmentName;
    Spinner sp_AssessmentType, sp_Month, sp_Day, sp_Year, sp_DueMonth, sp_DueDay, sp_DueYear;
    DatabaseHelper db;
    CheckBox cb_GoalDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_assessment_screen);
        Intent intent = getIntent();
        et_CourseName = findViewById(R.id.et_CourseName);
        et_AssessmentName = findViewById(R.id.et_AssesmentName);
        sp_Month = findViewById(R.id.sp_Month);
        sp_Day = findViewById(R.id.sp_Day);
        sp_Year = findViewById(R.id.sp_Year);

        cb_GoalDate = findViewById( R.id.cb_GoalDate);
        String courseName = intent.getStringExtra("courseName");
        et_CourseName.setText(courseName);

        ArrayAdapter<CharSequence> assessmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_types, android.R.layout.simple_spinner_item);
        assessmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_AssessmentType = findViewById(R.id.sp_AssessmentType);
        sp_AssessmentType.setAdapter(assessmentAdapter);

        ArrayAdapter<CharSequence> endMonthAdapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        endMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Month = findViewById(R.id.sp_Month);
        sp_Month.setAdapter(endMonthAdapter);

        ArrayAdapter<CharSequence> endDayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        endDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Day = findViewById(R.id.sp_Day);
        sp_Day.setAdapter(endDayAdapter);

        ArrayAdapter<CharSequence> endYearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        endYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Year = findViewById(R.id.sp_Year);
        sp_Year.setAdapter(endYearAdapter);

        sp_DueMonth = findViewById(R.id.sp_DueMonth);
        sp_DueDay = findViewById(R.id.sp_DueDay);
        sp_DueYear = findViewById(R.id.sp_DueYear);

        sp_DueMonth.setAdapter(endMonthAdapter);
        sp_DueDay.setAdapter(endDayAdapter);
        sp_DueYear.setAdapter(endYearAdapter);
    }

    public void btn_AddAssessment(View view){
        String goalDate = sp_Month.getSelectedItem().toString() + " " + sp_Day.getSelectedItem().toString() + ", " + sp_Year.getSelectedItem().toString();
        String dueDate = sp_DueMonth.getSelectedItem().toString() + " " + sp_DueDay.getSelectedItem().toString() + ", " + sp_DueYear.getSelectedItem().toString();
        String notify = "no";
        db =  new DatabaseHelper(this, null, null, 1);
        if(cb_GoalDate.isChecked()){
            notify = "yes";
        }
        db.insertAssessment(et_AssessmentName.getText().toString(), et_CourseName.getText().toString(), sp_AssessmentType.getSelectedItem().toString(), goalDate, notify, dueDate);
        Intent intent = new Intent(NewAssessmentScreen.this, CourseScreen.class);
        startActivity(intent);
    }
}
