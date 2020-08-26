package com.abhi.vedanam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DataAdapter extends FirebaseRecyclerAdapter<Realtimedata, DataAdapter.Myviewholder>{

    private OnItemcheck mcheck;
    Context context;

    public interface OnItemcheck {
        void onitem(View view,int position);
    }

    public void setOnitem(OnItemcheck itemcheck)
    {
        mcheck = itemcheck;
    }

     public DataAdapter(@NonNull FirebaseRecyclerOptions<Realtimedata> options) {
        super(options);
    }

    Realtimedata realtimedata;
    @Override
    protected void onBindViewHolder(@NonNull Myviewholder myviewholder, int i, @NonNull Realtimedata realtimedata) {
        this.realtimedata = realtimedata;
         myviewholder.teachername.setText(realtimedata.teacher_name);
         myviewholder.cname.setText(realtimedata.coaching);
         myviewholder.teacheraddress.setText(realtimedata.teacher_house +" "+realtimedata.teacher_street
                 +" "+realtimedata.teacher_area +" "+realtimedata.teacher_landmark +" "+realtimedata.teacher_pincode);
         myviewholder.teachersub.setText(realtimedata.subject);
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

         return new Myviewholder(view);
    }

    class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ImageView teacherimage;
         TextView teachername,cname,teacheraddress,teachersub;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            teacherimage = itemView.findViewById(R.id.teacherimage);
            teachername = itemView.findViewById(R.id.name);
            cname = itemView.findViewById(R.id.cname);
            teacheraddress = itemView.findViewById(R.id.address);
            teachersub = itemView.findViewById(R.id.subjectclass);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mcheck.onitem(v,getAdapterPosition());
        }
    }
}
