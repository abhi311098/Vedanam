package com.abhi.vedanam.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.abhi.vedanam.ChatData;
import com.abhi.vedanam.R;
import com.abhi.vedanam.model.Student;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class StudentQuery extends AppCompatActivity {

    RecyclerView recycler;
    ChatData adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_query);

        recycler = findViewById(R.id.re);
        recycler.setLayoutManager(new LinearLayoutManager(StudentQuery.this));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String id = user.getUid();
        final FirebaseRecyclerOptions<Student> options =
                new FirebaseRecyclerOptions.Builder<Student>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("chat").child(id), Student.class)
                        .build();

        adapter = new ChatData(options);
        recycler.setAdapter(adapter);

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