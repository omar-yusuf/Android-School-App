package com.example.omar.C196;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_7;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_1;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_2;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_3;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_4;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.TERM_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class TermEditScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    EditText et_StartDate, et_EndDate, et_TermName;
    TextView tv_termId;
    String startDate, endDate, termId, termName;
    ArrayList<String> checkedCourse = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_edit_screen);
        db =  new DatabaseHelper(this, null, null, 1);
        Intent intent = getIntent();
        termId = intent.getStringExtra("termId").trim();
        termName = intent.getStringExtra("termName");
        et_StartDate = findViewById(R.id.et_StartDate);
        et_EndDate = findViewById(R.id.et_EndDate);
        et_TermName = findViewById(R.id.et_TermName);
        tv_termId = findViewById(R.id.tv_TermId);
        ll = findViewById(R.id.edit_term_layout);

        String sql = "" +
                " SELECT * " +
                " FROM " + TERM_TABLE +
                " WHERE " + COL_TERM_1 + " = " + termId;
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        if(cursor.moveToFirst()){
            tv_termId.setText(cursor.getString(cursor.getColumnIndex(COL_TERM_4)));
            et_TermName.setText(cursor.getString(cursor.getColumnIndex(COL_TERM_4)));
            et_StartDate.setText(cursor.getString(cursor.getColumnIndex(COL_TERM_2)));
            et_EndDate.setText(cursor.getString(cursor.getColumnIndex(COL_TERM_3)));
        }
        sql = "" +
                " SELECT * " +
                " FROM " + COURSE_TABLE + ";";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        while(cursor.moveToNext()){
            tv_termId.setText("EDIT TERM " + termId);
            CheckBox cb_Course = new CheckBox(this);
            cb_Course.setText(cursor.getString(cursor.getColumnIndex("name")));
            String course_TermId = cursor.getString(cursor.getColumnIndex("TermId"));
            if(course_TermId != null && course_TermId.contains(termId)){
                cb_Course.setChecked(true);
            }
            ll.addView(cb_Course);
        }
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

    public void btn_EditTerm(View view){
        startDate = et_StartDate.getText().toString();
        endDate = et_EndDate.getText().toString();
        termName = et_TermName.getText().toString();
        String sql = "" +
                " UPDATE " + TERM_TABLE +
                " SET " + COL_TERM_2 + " = \"" + startDate + "\"," +
                COL_TERM_3 + " = \"" + endDate + "\", " +
                COL_TERM_4 + " = \"" + termName + "\"" +
                " WHERE " + COL_TERM_1 + " = \"" + termId + "\"";
        db.getWritableDatabase().execSQL(sql);
        sql = "" +
                " UPDATE " + COURSE_TABLE +
                " SET " + COL_COURSE_7 + " = NULL" +
                " WHERE " + COL_COURSE_7 + " = \"" + termId + "\"";
        db.getWritableDatabase().execSQL(sql);
        for(int i = 0; i < ll.getChildCount(); i++){
            View v = ll.getChildAt(i);
            if(v instanceof CheckBox){
                if(((CheckBox) v).isChecked()){
                    checkedCourse.add(((CheckBox) v).getText().toString());
                }
            }
        }
        for(int i = 0; i < checkedCourse.size(); i++){
            sql = "UPDATE " + COURSE_TABLE +
                    " SET " + COL_COURSE_7 + " = \"" + termId + "\"" +
                    " WHERE " + COL_COURSE_2 + " = \"" + checkedCourse.get(i) + "\"";
            db.getWritableDatabase().execSQL(sql);
        }
        Intent intent = new Intent(TermEditScreen.this, TermScreen.class);
        startActivity(intent);
    }
}
