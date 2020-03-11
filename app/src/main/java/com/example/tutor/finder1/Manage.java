package com.example.tutor.finder1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tutor.finder1.Connectivity.CheckConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Manage extends AppCompatActivity {
    Button bt_male,bt_female;
    DatabaseReference male;
    DatabaseReference female;
    FirebaseUser user;
    RelativeLayout linearLayout;
    Toolbar toolbar;
    TextView textView,textView_gfhf;
    TextView user_name;
    CircleImageView user_image;
    DatabaseReference reference_user;
    ProgressBar progressBar_manage;
    String image;
    String name;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        bt_female=findViewById(R.id.female_teacher);
        linearLayout=findViewById(R.id.manage_root);

        toolbar=findViewById(R.id.manage_toolbar);
        textView=findViewById(R.id.textView88);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Manage.this,AddActivity.class));
                Manage.this.finish();
            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account Management");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        user_name=findViewById(R.id.textView66);
        user_image=findViewById(R.id.circleImageView44);
        progressBar_manage=findViewById(R.id.progressBar29999999999);
        textView_gfhf=findViewById(R.id.textView5);
        bt_male=findViewById(R.id.male_teacher);
        user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid=user.getUid();
        reference_user=FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("info");
        male= FirebaseDatabase.getInstance().getReference().child("Button").child(uid);
        male.addValueEventListener(new ValueEventListener() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   String male=dataSnapshot.child("male").getValue().toString();
                   if (!male.equals("no")){
                     bt_male.setVisibility(View.VISIBLE);
                     textView.setVisibility(View.INVISIBLE);
                     textView_gfhf.setVisibility(View.INVISIBLE);

                   }
                   String female=dataSnapshot.child("female").getValue().toString();
                   if (!female.equals("no")){
                       bt_female.setVisibility(View.VISIBLE);
                       textView.setVisibility(View.INVISIBLE);
                       textView_gfhf.setVisibility(View.INVISIBLE);
                   }

               }catch (NullPointerException ignored){


               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }



    });
        bt_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String female="Male";
                Intent intent=new Intent(Manage.this,TutorAccountActivity.class);
                intent.putExtra("Male",female);
                startActivity(intent);
                Manage.this.finish();

            }
        });
        bt_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String male="Female";
                Intent intent=new Intent(Manage.this,TutorAccountActivity.class);
                intent.putExtra("Female",male);
                startActivity(intent);
                Manage.this.finish();
            }
        });

        reference_user.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image= Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                name= Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                if (image.isEmpty()) {
                    user_image.setImageResource(R.drawable.ic_action_avatar);
                } else{
                    Picasso.with(getBaseContext()).load(image).fit().centerCrop().into(user_image,new com.squareup.picasso.Callback(){

                        @Override
                        public void onSuccess() {
                            if (progressBar_manage!=null){
                                progressBar_manage.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                user_name.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }









    @Override
    protected void onStart() {
        super.onStart();
        if (!CheckConnection.isNetworkAvaliable(this)){
            Snackbar.make(linearLayout,"No internet",Snackbar.LENGTH_LONG).show();

        }
    }

}
