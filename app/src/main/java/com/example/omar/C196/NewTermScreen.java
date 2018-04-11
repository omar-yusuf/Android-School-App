package com.example.omar.C196;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_7;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_1;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.TERM_TABLE;

public class NewTermScreen extends AppCompatActivity {

    Spinner sp_StartMonth;
    Spinner sp_StartDay;
    Spinner sp_StartYear;
    Spinner sp_EndMonth;
    Spinner sp_EndDay;
    Spinner sp_EndYear;
    EditText et_TermName;
    LinearLayout ll;
    TextView tv_NoCourses;
    Button btn_CheckedCourse;
    ArrayList<String> checkedCourse = new ArrayList<>();
    DatabaseHelper db;
    int termId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_term_screen);

        ll = findViewById(R.id.course_layout);
        et_TermName = findViewById(R.id.et_TermName);

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

        db =  new DatabaseHelper(this, null, null, 1);
        final Cursor cursor = db.getTableData(COURSE_TABLE);
        tv_NoCourses = findViewById(R.id.tv_NoCourses);
        if(cursor.getCount() == 0) {
            tv_NoCourses.setText("No courses entered");
        } else {
            while(cursor.moveToNext()){
                CheckBox course = new CheckBox(this);
                course.setText(cursor.getString(cursor.getColumnIndex("name")));
                ll.addView(course);
            }
        }
    }

    public void btn_CheckedCourse(View view){
        String startDate = sp_StartMonth.getSelectedItem().toString() + " " + sp_StartDay.getSelectedItem().toString() + ", " + sp_StartYear.getSelectedItem().toString();
        String endDate = sp_EndMonth.getSelectedItem().toString() + " " + sp_EndDay.getSelectedItem().toString() + ", " + sp_EndYear.getSelectedItem().toString();
        String termName = et_TermName.getText().toString();
        db.insertTerm(startDate, endDate, termName);
        checkedCourse.clear();
        btn_CheckedCourse = findViewById(R.id.btn_CheckedCourse);
        for(int i = 0; i < ll.getChildCount(); i++){
            CheckBox cb = (CheckBox) ll.getChildAt(i);
            if(cb.isChecked()){
                checkedCourse.add(cb.getText().toString());
            }
        }
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT MAX(" + COL_TERM_1 + ")" +
                     "FROM " + TERM_TABLE, null);
        cursor.moveToFirst();
        termId = cursor.getInt(0);
        for(int i = 0; i < checkedCourse.size(); i++){
            db.getWritableDatabase().execSQL("" +
                    " UPDATE " + COURSE_TABLE +
                    " SET " + COL_COURSE_7 + "= " + termId +
                    " WHERE " + COL_COURSE_2 + "= " + "\"" + checkedCourse.get(i) + "\"");
        }
        Intent intent = new Intent(NewTermScreen.this, TermScreen.class);
        startActivity(intent);
    }
}
