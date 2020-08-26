package com.abhi.vedanam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.abhi.vedanam.student.HomeStudent;
import com.abhi.vedanam.student.LoginPageStudent;
import com.abhi.vedanam.teacher.HomeTeacher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(ui);

        getSupportActionBar().hide();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.custom_anima);
        ImageView imageView = findViewById(R.id.vidiam);
        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("impkey", Context.MODE_PRIVATE);
                final String impkey = sharedPreferences.getString("key", null);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("checking", "" + user);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("teacher").child(impkey);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String loginkey = snapshot.child("impkey").getValue(String.class);
                            if (snapshot.exists()) {
                                if (loginkey != null && loginkey.equals(impkey)) {
                                    Intent intent = new Intent(MainActivity.this, HomeTeacher.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Intent intent = new Intent(MainActivity.this, HomeStudent.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SharedPreferences sharedPreferences = getSharedPreferences("impkey", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor;
                            editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, Verify.class);
                            startActivity(i);
                        }
                    });

                }
            }
        }, 2200);
    }
}