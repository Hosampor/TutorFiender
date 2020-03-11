package com.example.tutor.finder1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tutor.finder1.FemaleHolder.CommentHolder;
import com.example.tutor.finder1.Helper.CommentDBHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class commentStudent extends AppCompatActivity {

    TextInputLayout edit_comment;
    ImageButton imageButton_send_comment;
    FirebaseUser user;

    String uid;
    DatabaseReference reference_user,reference_comment,reference,reference_like_dislike;
    String name;
    String image;
    String timestamp;
    String like="0";
    String dislike="0";
    RelativeLayout layout;
    String key="";
    RecyclerView recyclerView;
    ImageView imageView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<CommentDBHelper,CommentHolder> adapter;
    EditText editText_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_student);
        recyclerView=findViewById(R.id.recyclerview_comment);
        recyclerView.setHasFixedSize(true);
        imageView=findViewById(R.id.back_button);
        imageButton_send_comment=findViewById(R.id.push_comment);
        layoutManager=new LinearLayoutManager(commentStudent.this);
        recyclerView.setLayoutManager(layoutManager);
        key=getIntent().getStringExtra("key");
        user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid=user.getUid();

        editText_comment=findViewById(R.id.et_comment);

        reference_user=FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("info");



        reference_comment=FirebaseDatabase.getInstance().getReference().child("Comment").child(key).child(uid);

        reference=FirebaseDatabase.getInstance().getReference().child("Comment").child(key);


        reference.keepSynced(true);

        layout=findViewById(R.id.rellllllll);


        edit_comment=findViewById(R.id.edit_comment);


        imageButton_send_comment=findViewById(R.id.push_comment);


        timestamp = new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Calendar.getInstance().getTime());


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(commentStudent.this,Student_Details_Activity.class);
                intent.putExtra("taizul",key);
                startActivity(intent);
                finish();
            }
        });
        reference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue().toString();
                image=dataSnapshot.child("image").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        load();


        imageButton_send_comment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String comment= Objects.requireNonNull(edit_comment.getEditText()).getText().toString();
                CommentDBHelper commentDBHelper=new CommentDBHelper(image,name,timestamp,comment,like,dislike);
                reference_comment.setValue(commentDBHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            editText_comment.setText("");

                            Snackbar.make(layout,"Comment complited",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });




    }

    private void load() {
        adapter=new FirebaseRecyclerAdapter<CommentDBHelper, CommentHolder>(CommentDBHelper.class,R.layout.comment,CommentHolder.class,reference) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, CommentDBHelper model, final int position) {



                if (model.getImage_commenter().isEmpty()) {
                    viewHolder.imageView_user.setImageResource(R.drawable.ic_action_avatar);
                } else{
                    Picasso.with(getBaseContext()).load(model.getImage_commenter()).fit().centerCrop().into(viewHolder.imageView_user);
                }




                viewHolder.username.setText(model.getCommenter_name());


                String time=model.getPosting_time();
                String difference = "";
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
                Date today = c.getTime();
                sdf.format(today);
                Date timestamp;
                try {
                    timestamp = sdf.parse(time);
                    difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
                } catch (ParseException e) {
                    difference = "0";
                }
                if (!difference.equals("0")) {
                    viewHolder.posting_time.setText( difference+ " days ago");
                } else {
                    viewHolder.posting_time.setText("Today");
                }





                viewHolder.user_comment.setText(model.getComment());






                final String current_key=adapter.getRef(position).getKey();


                final DatabaseReference referencedislike=FirebaseDatabase.getInstance().getReference().child("TutorDislike").child(current_key).child(uid).child("dislike");


                final DatabaseReference referencedislikecount=FirebaseDatabase.getInstance().getReference().child("TutorDislike").child(current_key);



                final DatabaseReference referencelike=FirebaseDatabase.getInstance().getReference().child("TutorLike").child(current_key).child(uid).child("like");



                final DatabaseReference reference1likcount=FirebaseDatabase.getInstance().getReference().child("TutorLike").child(current_key);


                referencedislike.keepSynced(true);
                referencedislikecount.keepSynced(true);
                referencelike.keepSynced(true);
                reference1likcount.keepSynced(true);


                final long[] dislike = new long[1];
                referencedislikecount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dislike[0] = dataSnapshot.getChildrenCount();
                        String nna=Long.toString(dislike[0]);
                        viewHolder.user_dislike.setText(nna);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                reference1likcount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long av= dataSnapshot.getChildrenCount();
                        String nna=Long.toString(av);
                        viewHolder.user_like.setText(nna);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.btn_dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.btn_dislike.setImageResource(R.drawable.dislike1);
                        referencedislike.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    referencelike.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            viewHolder.btn_like.setImageResource(R.drawable.like);
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Disliked",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });


                    }
                });
                viewHolder.btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.btn_like.setImageResource(R.drawable.like1);
                        referencelike.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    referencedislike.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                viewHolder.btn_dislike.setImageResource(R.drawable.dislike);
                                                Toast.makeText(commentStudent.this,"Liked",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });




                    }
                });





            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


}
