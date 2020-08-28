package com.abhi.vedanam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.vedanam.model.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatData extends FirebaseRecyclerAdapter<Student, ChatData.MyView> {

    public ChatData(@NonNull FirebaseRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyView myView, int i, @NonNull Student student) {
        myView.sn.setText(student.getStudentname());
        myView.se.setText(student.getEmail());
        myView.snu.setText(student.getNumber());
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem,parent,false);
        return new ChatData.MyView(view);
    }

    class MyView extends RecyclerView.ViewHolder {

        TextView sn, se, snu;

        public MyView(@NonNull View itemView) {
            super(itemView);
            sn = itemView.findViewById(R.id.sn);
            se = itemView.findViewById(R.id.se);
            snu = itemView.findViewById(R.id.snu);
        }
    }

}
