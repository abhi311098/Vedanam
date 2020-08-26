package com.abhi.vedanam.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.vedanam.DataAdapter;
import com.abhi.vedanam.R;
import com.abhi.vedanam.Realtimedata;
import com.abhi.vedanam.teacher.ResetPasswordTeacher;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HomeStudent extends AppCompatActivity  {

    private static final String TAG = "homestudent";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerOptions<Realtimedata> options =
                new FirebaseRecyclerOptions.Builder<Realtimedata>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherProfile"), Realtimedata.class)
                        .build();

        adapter = new DataAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnitem(new DataAdapter.OnItemcheck() {
            @Override
            public void onitem(View view, int position) {
                Intent intent = new Intent(HomeStudent.this, TeacherDetails.class);
                intent.putExtra("teacherdetailkey", options.getSnapshots().get(position));
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(HomeStudent.this,ProfileStudent.class);
                startActivity(intent);
                break;
            case R.id.item2:
                Intent i = new Intent(HomeStudent.this, ResetPasswordTeacher.class);
                startActivity(i);
                finish();
                break;

            case R.id.item3:
                deleteuser();
                break;

            case R.id.item4:
                googleLogout();
                break;
            default:
                Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteuser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            SharedPreferences sharedPreferences = getSharedPreferences("impkey", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(HomeStudent.this,LoginPageStudent.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void googleLogout() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                preferences = getSharedPreferences("impkey", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(HomeStudent.this,LoginPageStudent.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
