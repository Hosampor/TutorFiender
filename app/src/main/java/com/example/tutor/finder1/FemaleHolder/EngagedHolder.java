package com.example.tutor.finder1.FemaleHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tutor.finder1.Interface.ItemClickListener;
import com.example.tutor.finder1.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rana on 28-01-18.
 */

public class EngagedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tv_name,tv_institute_name,tv_date_enagge,tv_amount_engaged,tv_mobile;
    public CircleImageView imageView_engage;
    public ItemClickListener itemClickListener;

    public EngagedHolder(View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.tv_name_engaged);
        tv_institute_name=itemView.findViewById(R.id.tv_Institute_name_engaged);
        tv_date_enagge=itemView.findViewById(R.id.tv_date_engaged);
        tv_amount_engaged=itemView.findViewById(R.id.tv_amount_engaged);
        imageView_engage=itemView.findViewById(R.id.imageView_engaged);
        tv_mobile=itemView.findViewById(R.id.tv_mobile);
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
