package com.abhi.vedanam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abhi.vedanam.student.HomeStudent;
import com.abhi.vedanam.student.LoginPageStudent;
import com.abhi.vedanam.teacher.HomeTeacher;
import com.abhi.vedanam.teacher.LoginPageTeacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Verify extends AppCompatActivity {

    TextView teacher,student;
    String impkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        teacher = findViewById(R.id.teacherbox);
        student = findViewById(R.id.studentbox);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Verify.this, LoginPageStudent.class);
                startActivity(i);
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacher = "teacher";
                Intent i = new Intent(Verify.this, LoginPageTeacher.class);
                i.putExtra("loginkey",teacher);
                Toast.makeText(Verify.this, ""+teacher, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }
}