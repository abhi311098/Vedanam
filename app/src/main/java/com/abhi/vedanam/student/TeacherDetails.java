package com.abhi.vedanam.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.abhi.vedanam.R;
import com.abhi.vedanam.Realtimedata;

public class TeacherDetails extends AppCompatActivity {

    private TextView tname,tcname,taddress,tsubject,temailandnumber;
    Realtimedata data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);

        data = (Realtimedata)getIntent().getExtras().getSerializable("teacherdetailkey");

        tname = findViewById(R.id.teachername);
        tcname = findViewById(R.id.teachercoachingname);
        taddress = findViewById(R.id.teacheraddress);
        tsubject = findViewById(R.id.teachersubject);
        temailandnumber = findViewById(R.id.teacheremailandnumber);

        tname.setText("Teacher Name: "+data.getTeacher_name());
        tcname.setText("Coaching Center Name: "+data.getCoaching());
        taddress.setText("Coaching Address: "+data.getTeacher_house()+" "+data.getTeacher_street()+" " +
                " "+data.getTeacher_area()+" near "+data.getTeacher_landmark()+" "+data.getTeacher_pincode());
        tsubject.setText("We Teach Us "+data.getSubject());
        temailandnumber.setText("Email: "+data.getTeacher_email()+"\n"+"Phone Number: "+data.getTeacher_number());
    }

    public void querywithteacher(View view){

        String t_uid = data.getTeacher_uid();


        Intent intent = new Intent(this,QueryWithTeacher.class);
        intent.putExtra("coachingnamekey",data.getCoaching());
        intent.putExtra("emailkey",data.getTeacher_email());
        intent.putExtra("numberkey",data.getTeacher_number());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return true;
    }
}