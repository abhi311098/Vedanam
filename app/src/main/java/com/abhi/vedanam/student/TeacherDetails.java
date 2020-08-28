package com.abhi.vedanam.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.vedanam.R;
import com.abhi.vedanam.Realtimedata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    public void chatroom(View view){

        SharedPreferences preferences = getSharedPreferences("StudentProfileDetails", Context.MODE_PRIVATE);
        String s_name = preferences.getString("student_name", null);
        String s_email = preferences.getString("student_email", null);
        String s_number = preferences.getString("student_number", null);

        String id = data.getTeacher_uid();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("student_name", s_name);
        hm.put("student_email", s_email);
        hm.put("student_number", s_number);

        Toast.makeText(this, s_name+" "+s_email+" "+" "+s_number, Toast.LENGTH_SHORT).show();

        SharedPreferences preferences1 = getSharedPreferences("impkey", Context.MODE_PRIVATE);
        String sid = preferences1.getString("impkey",null);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String s = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chat").child(id).push();

        Log.e("taaaaaaaad", id);
        reference.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(TeacherDetails.this, "congo", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("td",e.getMessage());
            }
        });

        Toast.makeText(this, "mast", Toast.LENGTH_SHORT).show();
    }
}
