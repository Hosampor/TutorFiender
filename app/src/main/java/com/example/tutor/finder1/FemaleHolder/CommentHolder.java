package com.example.tutor.finder1.FemaleHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tutor.finder1.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rana on 10-03-18.
 */

public class CommentHolder extends RecyclerView.ViewHolder {
    public TextView username,user_comment,posting_time,user_like,user_dislike;
    public ImageButton btn_like,btn_dislike;
    public CircleImageView imageView_user;
    public CommentHolder(View itemView) {
        super(itemView);
        imageView_user=itemView.findViewById(R.id.image_comment);
        username=itemView.findViewById(R.id.name_comment);
        btn_like=itemView.findViewById(R.id.button_like_comment);
        btn_dislike=itemView.findViewById(R.id.button_dislike_comment);
        posting_time=itemView.findViewById(R.id.time_comment);
        user_comment=itemView.findViewById(R.id.comment_comment);
        user_like=itemView.findViewById(R.id.like_comment);
        user_dislike=itemView.findViewById(R.id.dislike_comment);

    }


}
