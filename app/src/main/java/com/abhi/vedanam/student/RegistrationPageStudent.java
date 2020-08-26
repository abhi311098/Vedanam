package com.abhi.vedanam.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abhi.vedanam.R;
import com.abhi.vedanam.teacher.HomeTeacher;
import com.abhi.vedanam.teacher.RegistrationPageTeacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationPageStudent extends AppCompatActivity {

    ProgressDialog progressDialog;
    private EditText remail, rpassword, rcpassword;
    private Button reg;
    FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String TAG = "registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page_student);

        remail = findViewById(R.id.registrationemail);
        rpassword = findViewById(R.id.registrationpassword);
        rcpassword = findViewById(R.id.registrationpconfirmassword);
        reg = findViewById(R.id.registrationbutton2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_view);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mAuth = FirebaseAuth.getInstance();

                String email = remail.getText().toString().trim();
                String password = rpassword.getText().toString();
                String cpassword = rcpassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "enter email address", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "not valid email address", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "enter password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cpassword)) {
                    Toast.makeText(RegistrationPageStudent.this, "password and confirm password are not match", Toast.LENGTH_SHORT).show();
                } else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        loginkeymethod();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationPageStudent.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                    // ...
                                }
                            });
                }
            }
        });
    }

    private void loginkeymethod() {
        FirebaseUser user = mAuth.getCurrentUser();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("impkey",user.getUid());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("student").child(user.getUid());
        databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationPageStudent.this, "done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationPageStudent.this, HomeStudent.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationPageStudent.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
