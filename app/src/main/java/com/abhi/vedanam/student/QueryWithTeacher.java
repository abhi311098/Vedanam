package com.abhi.vedanam.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abhi.vedanam.R;

public class QueryWithTeacher extends AppCompatActivity {

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_with_teacher);

        Bundle bundle = getIntent().getExtras();
        String cname = bundle.getString("coachingnamekey");
        String email = bundle.getString("emailkey");
        number = bundle.getString("numberkey");

        TextView textView = findViewById(R.id.detail);
        textView.setText("Coaching Name: "+cname+"\n"+"Email: "+email+"\n"+"Phone Number: "+number);

    }

    public void callteacher(View view){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+number));
        if (ActivityCompat.checkSelfPermission(QueryWithTeacher.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(QueryWithTeacher.this,new String[]{Manifest.permission.CALL_PHONE},0);
            return;
        }
        startActivity(i);
    }
}
