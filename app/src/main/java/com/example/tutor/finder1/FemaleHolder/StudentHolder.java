package com.example.tutor.finder1.FemaleHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tutor.finder1.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentHolder extends RecyclerView.ViewHolder {
    public CircleImageView circleImageView;
    public ProgressBar progressBar;
    public TextView tv_st_name,tv_st_institute_name,tv_st_class,tv_st_days,tv_st_ago;
    public StudentHolder(View itemView) {
        super(itemView);
        tv_st_name=itemView.findViewById(R.id.tv_name_st);
        tv_st_institute_name=itemView.findViewById(R.id.tv_Institute_name_st);
        tv_st_class=itemView.findViewById(R.id.tv_class_st);
        tv_st_days=itemView.findViewById(R.id.tv_days);
        tv_st_ago=itemView.findViewById(R.id.tv_days_ago);
        circleImageView=itemView.findViewById(R.id.imageView_st);
        progressBar=itemView.findViewById(R.id.progressBar_merge);
    }
}
