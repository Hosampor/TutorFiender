package com.example.tutor.finder1.FemaleHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tutor.finder1.Interface.ItemClickListener;
import com.example.tutor.finder1.R;

import de.hdodenhof.circleimageview.CircleImageView;



public class FemaleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

     public TextView tv_name,tv_institute_name,tv_subject,tv_gender,tv_housing;
     public CircleImageView imageView_female;
     public ProgressBar progressBar;
     public ItemClickListener itemClickListener;
    public FemaleHolder(View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.tv_name);
        tv_institute_name=itemView.findViewById(R.id.tv_Institute_name);
        tv_subject=itemView.findViewById(R.id.tv_subject);
        tv_gender=itemView.findViewById(R.id.tv_gender);
        tv_housing=itemView.findViewById(R.id.tv_housing);
        imageView_female=itemView.findViewById(R.id.imageView_st);

        progressBar=itemView.findViewById(R.id.progressBar_merge);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


}
