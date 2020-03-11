package com.example.tutor.finder1;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.example.tutor.finder1.FemaleHolder.EngagedHolder;
import com.example.tutor.finder1.Helper.Token;
import com.example.tutor.finder1.Helper.UserInformation;
import com.example.tutor.finder1.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;
public class Student_Details_Activity extends AppCompatActivity {
    RelativeLayout layout_expand111,layout_expand222;
    TextView item_description_title,item_description_title111;
    JustifyTextView item_description;
    Animation animationUp,animationDown;
    FirebaseRecyclerAdapter<UserInformation,EngagedHolder> adapter;
    DatabaseReference reference_tutor,reference_like,reference_like_counter,reference_follower,reference_engaged;
    CircleImageView circleImageView1111;
    LikeButton likeButton;
    TextView tutor_profile_name1,tutor_email,tutor_contact;
    ScrollView scrollView;
    FirebaseUser user_tutor;
    String user_id;
    List<Token> tokens=new ArrayList<>();
    List<Token> follower=new ArrayList<>();
    LinearLayoutManager mlayoutManager;
    List<UserInformation> information=new ArrayList<>();
    TextView likes,follower_list;
    int counterss;
    RecyclerView recyclerView_engaged;
    DatabaseReference reference_engage;
    public String engage11;
    ImageButton buttonnnnnl;
    Button button_engage;
    TextView textView_eng;
    String tutor_key="";
    String taka_tutor;
    String identifier_home="";
    DatabaseReference reference_engaged_count,reference_check;
    String contact;
    ProgressBar progressBar_tutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__details_);
        circleImageView1111=findViewById(R.id.tutor_profile_image);
        tutor_email=findViewById(R.id.tutor_email);
        tutor_contact=findViewById(R.id.tutor_contact_no);
        layout_expand222=findViewById(R.id.layout_expand1);
        item_description_title111=findViewById(R.id.item_description_title1);
        likeButton=findViewById(R.id.star_button);
        follower_list=findViewById(R.id.folloers_list);
        user_tutor= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView_engaged=findViewById(R.id.item_description1_recycler);
        taka_tutor="Amount: Negotiable";
        tutor_key=getIntent().getStringExtra("taizul");
        identifier_home=getIntent().getStringExtra("home");
        recyclerView_engaged.setHasFixedSize(true);
        mlayoutManager=new LinearLayoutManager(this);
        recyclerView_engaged.setLayoutManager(mlayoutManager);
        user_id=user_tutor.getUid();
        buttonnnnnl=findViewById(R.id.back_button);
        buttonnnnnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Details_Activity.this,Home_2.class));
                finish();
            }
        });
        button_engage=findViewById(R.id.btn_engage);
        progressBar_tutor=findViewById(R.id.progressBar2333333);
        textView_eng=findViewById(R.id.textView_engage);
        scrollView=findViewById(R.id.scrolview);
        likes=findViewById(R.id.likes_counter);
        tutor_profile_name1=findViewById(R.id.tutor_profile_name);
        reference_tutor= FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child("Student").child(tutor_key);
        reference_like=FirebaseDatabase.getInstance().getReference().child("Like").child(tutor_key).child(user_id).child("like");
        reference_engaged=FirebaseDatabase.getInstance().getReference().child("Engaged").child(tutor_key).child(user_id);
        reference_engaged_count=FirebaseDatabase.getInstance().getReference().child("Engaged").child(tutor_key);
        reference_follower=FirebaseDatabase.getInstance().getReference().child("Followers").child(tutor_key);
        reference_like_counter=FirebaseDatabase.getInstance().getReference().child("Like").child(tutor_key);
        reference_engage= FirebaseDatabase.getInstance().getReference().child("user").child(user_id).child("info");
        reference_check=FirebaseDatabase.getInstance().getReference().child("Engaged").child(tutor_key).child(user_id);
        layout_expand111 = findViewById(R.id.layout_expand);
        item_description = findViewById(R.id.item_description);
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        item_description_title =  findViewById(R.id.item_description_title);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Token token=new Token("jijo","kljlj");
                reference_like.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                reference_like.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(scrollView,"Unliked",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });



        layout_expand111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item_description.isShown()){
                    item_description.setVisibility(View.GONE);
                    item_description.startAnimation(animationUp);
                }
                else{
                    item_description.setVisibility(View.VISIBLE);
                    item_description.startAnimation(animationDown);
                }
            }
        });



        item_description_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item_description.isShown()){
                    item_description.setVisibility(View.GONE);
                    item_description.startAnimation(animationUp);
                }
                else{
                    item_description.setVisibility(View.VISIBLE);
                    item_description.startAnimation(animationDown);
                }
            }
        });



        layout_expand222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView_engaged.isShown()){
                    recyclerView_engaged.setVisibility(View.GONE);
                    recyclerView_engaged.startAnimation(animationUp);
                    engagedStudent();
                }
                else{
                    recyclerView_engaged.setVisibility(View.VISIBLE);
                    recyclerView_engaged.startAnimation(animationDown);
                    engagedStudent();



                }
            }
        });


        item_description_title111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView_engaged.isShown()){
                    recyclerView_engaged.setVisibility(View.GONE);
                    recyclerView_engaged.startAnimation(animationUp);
                    engagedStudent();
                }
                else{
                    recyclerView_engaged.setVisibility(View.VISIBLE);
                    recyclerView_engaged.startAnimation(animationDown);
                    engagedStudent();
                }
            }
        });



        reference_tutor.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String profile_image= Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                    if (profile_image.isEmpty()) {
                        circleImageView1111.setImageResource(R.drawable.ic_action_avatar);
                    } else{
                        Picasso.with(getBaseContext()).load(profile_image).fit().centerCrop().into(circleImageView1111,new com.squareup.picasso.Callback(){

                            @Override
                            public void onSuccess() {
                                if (progressBar_tutor!=null){
                                    progressBar_tutor.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                    //String age= dataSnapshot.child("age").getValue().toString();
                    String institute= dataSnapshot.child("institute_name").getValue().toString();
                    //String sub= dataSnapshot.child("subject").getValue().toString();
                    //String housing_name= dataSnapshot.child("housing_name").getValue().toString();
                    String class_name= dataSnapshot.child("class_name").getValue().toString();
                    //String subject= Objects.requireNonNull(dataSnapshot.child("interested_subject").getValue()).toString();
                    String name= dataSnapshot.child("name").getValue().toString();
                    String mail=dataSnapshot.child("email").getValue().toString();
                    String num=dataSnapshot.child("phone").getValue().toString();
                    tutor_email.setText(mail);
                    tutor_contact.setText(num);

                    tutor_profile_name1.setText(name);
                    item_description.setText("Hello I am " +name+ ". I am studying at "+institute+".  I read in class "+class_name+". If you want to engaged with me so please like me and then engage with me.");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        reference_follower.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try {
                        Token token=snapshot.getValue(Token.class);
                        follower.add(token);
                        final int counterss1111=follower.size();
                        final String number111=String.valueOf(counterss1111);
                        follower_list.setText(number111);
                    }catch (DatabaseException ignored){

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        reference_check.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String values= Objects.requireNonNull(dataSnapshot.child("state").getValue()).toString();
                    if (values.equals("yes")){
                        reference_tutor.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String email= Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                                tutor_email.setText(email);
                                String contact= Objects.requireNonNull(dataSnapshot.child("contact").getValue()).toString();
                                tutor_contact.setText(contact);
                                button_engage.setText("Engaged");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        reference_engaged_count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try {
                        UserInformation informations=snapshot.getValue(UserInformation.class);
                        information.add(informations);
                        final int counterss11111=information.size();
                        final String number1111=String.valueOf(counterss11111);
                        textView_eng.setText(number1111);
                        if (counterss11111==6){
                            button_engage.setEnabled(false);
                            textView_eng.setTextColor(ContextCompat.getColor(Student_Details_Activity.this, R.color.red));
                        }
                    }catch (DatabaseException ignored){

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        reference_like_counter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try {
                        Token token=snapshot.getValue(Token.class);

                        tokens.add(token);
                        final int counterss11=tokens.size();
                        final String number11=String.valueOf(counterss11);
                        likes.setText(number11);
                    }catch (DatabaseException ignored){

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }



    private void engagedStudent() {
        adapter=new FirebaseRecyclerAdapter<UserInformation, EngagedHolder>(UserInformation.class,R.layout.engaged_adapter,EngagedHolder.class,reference_engaged_count) {
            @Override
            protected void populateViewHolder(EngagedHolder viewHolder, UserInformation model, int position) {
                viewHolder.tv_name.setText(model.getName());
                viewHolder.tv_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_date_enagge.setText(model.getDate());
                viewHolder.tv_amount_engaged.setText(model.getAmount());
                viewHolder.tv_mobile.setText(model.getMobile());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView_engage);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getApplicationContext(),"Our teacher is a good man",Toast.LENGTH_LONG).show();

                    }
                });
            }
        };
        recyclerView_engaged.setAdapter(adapter);

    }




    private void save(final String name, String image, String institute, String date, String taka,String mobile) {

        UserInformation userInformation=new UserInformation(name,image,institute,date,taka,mobile);
        reference_engaged.setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference_tutor.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value="yes";
                            reference_check.child("state").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        button_engage.setText("Engaged ");
                                        Snackbar.make(scrollView,"Welcome "+name+".",Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }



    public void send_email(View view) {
        String subject ="";
        String body ="";

        String email_tutor=tutor_email.getText().toString();
//        String fake_email="bdtutor@example.com";
//        if (email_tutor.equals(fake_email)){
//            Snackbar.make(scrollView,"To send email me,first you need to engage with me",Snackbar.LENGTH_LONG).show();
//        }
//        else {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email_tutor));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, "Choose One"));
//        }
    }



    public void phone_call_dialer(View view) {
        contact = tutor_contact.getText().toString();
        //String fake_contact = "+8801xxxxxxxxx";
        //if (contact.equals(fake_contact)) {
         //   Snackbar.make(scrollView, "To contact with me,first you need to engage with me", Snackbar.LENGTH_LONG).show();
        //} else {
            onCallBtnClick();
        //}
    }



    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {
            if (ActivityCompat.checkSelfPermission(Student_Details_Activity.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                ActivityCompat.requestPermissions(Student_Details_Activity.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {

            Snackbar.make(scrollView, "You don't assign permission.", Snackbar.LENGTH_LONG).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(Student_Details_Activity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+contact));
            Student_Details_Activity.this.startActivity(callIntent);
        }else{
            Snackbar.make(scrollView, "You don't assign permission.", Snackbar.LENGTH_LONG).show();
        }
    }



    public void coment_tutor(View view) {


        Intent intent=new Intent(Student_Details_Activity.this,commentStudent.class);
        intent.putExtra("key",tutor_key);
        startActivity(intent);



    }

    public void go_tutor_detail_activity(View view) {
        if (identifier_home!=null && identifier_home.equalsIgnoreCase("home")) {
            Intent intent = new Intent(Student_Details_Activity.this, Home.class);
            startActivity(intent);
            finish();
        }else if (identifier_home!=null&& identifier_home.equalsIgnoreCase("female")){
            Intent intent=new Intent(Student_Details_Activity.this,Female.class);
            startActivity(intent);
            finish();
        }
        else if (identifier_home!=null&& identifier_home.equalsIgnoreCase("male")){
            Intent intent=new Intent(Student_Details_Activity.this,Male.class);
            startActivity(intent);
            finish();
        }
        else if (identifier_home!=null&& identifier_home.equalsIgnoreCase("map")){
            Intent intent=new Intent(Student_Details_Activity.this,MapsActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
