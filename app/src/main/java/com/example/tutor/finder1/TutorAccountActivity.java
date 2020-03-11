package com.example.tutor.finder1;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tutor.finder1.FemaleHolder.EngagedHolder;
import com.example.tutor.finder1.Helper.Token;
import com.example.tutor.finder1.Helper.UserInformation;
import com.example.tutor.finder1.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class TutorAccountActivity extends AppCompatActivity {

    DatabaseReference reference_tutor,reference_like_counter,reference_engaged_count,reference_follower,reference_female,reference_male,reference_location,reference_porate,reference_check;
    FirebaseUser firebaseUser;
    String user_id;
    RelativeLayout layout_expand111,layout_expand222;
    TextView item_description_title,item_description_title111,tutor_name,tutor_address,tutor_account_likes,tutor_account_engage,tutor_account_follow;
    JustifyTextView item_description;
    Animation animationUp,animationDown;
    RecyclerView recyclerView_engaged;
    LinearLayoutManager mlayoutManager;
    CircleImageView image_tutor_account;
    ScrollView scrollView;
    ProgressBar progressBar_account;
    String male="";
    String female="";
    String choose1="Male";
    String choose2="Female";
    List<Token> likes=new ArrayList<>();
    List<Token> followers=new ArrayList<>();
    List<UserInformation> engaged=new ArrayList<>();
    FirebaseRecyclerAdapter<UserInformation,EngagedHolder> adapter;
    Dialog dialog,engaged_dialog;
    String profile_image;
    String name;
    String mobile_no="";
    TextView tutorRating;
    DatabaseReference tuRat;
    ProgressDialog dialog_dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_account_activity);
        recyclerView_engaged=findViewById(R.id.item_description1_recycler);
        recyclerView_engaged.setHasFixedSize(true);
        mlayoutManager=new LinearLayoutManager(this);
        recyclerView_engaged.setLayoutManager(mlayoutManager);
        image_tutor_account=findViewById(R.id.imageView_tutor_account);
        layout_expand111 = findViewById(R.id.layout_expand);
        progressBar_account=findViewById(R.id.progressBar2555555555);
        item_description = findViewById(R.id.item_description);
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        item_description_title =  findViewById(R.id.item_description_title);
        layout_expand222=findViewById(R.id.layout_expand1);
        item_description_title111=findViewById(R.id.item_description_title1);
        tutor_name=findViewById(R.id.tv_name);
        tutor_address=findViewById(R.id.tv_address);
        tutor_account_likes=findViewById(R.id.tutor_account_like);
        tutor_account_engage=findViewById(R.id.tutor_account_engaged);
        tutor_account_follow=findViewById(R.id.tutor_account_followers);
        dialog=new Dialog(TutorAccountActivity.this);
        engaged_dialog=new Dialog(TutorAccountActivity.this);
        scrollView=findViewById(R.id.tutor_account_details);
        dialog_dialog=new ProgressDialog(TutorAccountActivity.this);
        dialog_dialog.setTitle("Deleting");
        dialog_dialog.setMessage("Please wait..");
        male=getIntent().getStringExtra("Male");
        female=getIntent().getStringExtra("Female");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        tutorRating=findViewById(R.id.tutor_account_rating);
        assert firebaseUser != null;
        user_id=firebaseUser.getUid();
        if (male!=null){
            reference_tutor= FirebaseDatabase.getInstance().getReference().child(male).child(user_id);
        }
        else if (female!=null){
            reference_tutor= FirebaseDatabase.getInstance().getReference().child(female).child(user_id);
        }

        reference_like_counter=FirebaseDatabase.getInstance().getReference().child("Like").child(user_id);
        reference_check=FirebaseDatabase.getInstance().getReference().child("user").child(user_id).child(user_id);
        reference_engaged_count=FirebaseDatabase.getInstance().getReference().child("Engaged").child(user_id);
        reference_follower=FirebaseDatabase.getInstance().getReference().child("Followers").child(user_id);
        reference_female=FirebaseDatabase.getInstance().getReference().child("Female").child(user_id);
        reference_male=FirebaseDatabase.getInstance().getReference().child("Male").child(user_id);
        reference_location=FirebaseDatabase.getInstance().getReference().child("location").child(user_id);
        reference_porate=FirebaseDatabase.getInstance().getReference().child("poratechai").child(user_id);


//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        tuRat=FirebaseDatabase.getInstance().getReference().child("ratting").child(user_id).child("clint");
        tuRat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String a=dataSnapshot.getValue().toString();
//                ratText.setText(a);
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;
                double round=0.0;
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    //String a=dataSnapshot.getValue().toString();
                    double b=Double.parseDouble(ds.child("id").getValue().toString());
                    total=total+b;
                    count=count+1;
                    average=total/count;
                    round=Math.round(average*100.0)/100.0;
                }

                String finalresult=Double.toString(round);
                tutorRating.setText(finalresult);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

        reference_follower.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try {
                        Token token=snapshot.getValue(Token.class);
                        followers.add(token);
                        final int counterss1111=followers.size();
                        final String number111=String.valueOf(counterss1111);
                        tutor_account_follow.setText(number111);
                    }catch (DatabaseException ignored){

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    profile_image= Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    if (profile_image.isEmpty()) {
                        image_tutor_account.setImageResource(R.drawable.ic_action_avatar);
                    } else{
                        Picasso.with(getBaseContext()).load(profile_image).fit().centerCrop().into(image_tutor_account,new com.squareup.picasso.Callback(){

                            @Override
                            public void onSuccess() {
                                if (progressBar_account!=null){
                                    progressBar_account.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                    String age= Objects.requireNonNull(dataSnapshot.child("age").getValue()).toString();
                    String institute= Objects.requireNonNull(dataSnapshot.child("institute_name").getValue()).toString();
                    String sub= Objects.requireNonNull(dataSnapshot.child("subject").getValue()).toString();
                    String housing_name= Objects.requireNonNull(dataSnapshot.child("housing_name").getValue()).toString();
                    String class_name= Objects.requireNonNull(dataSnapshot.child("interested_class").getValue()).toString();
                    String subject= Objects.requireNonNull(dataSnapshot.child("interested_subject").getValue()).toString();
                    name= Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    tutor_name.setText(name);
                    tutor_address.setText(housing_name);
                    item_description.setText("Hello I am " +name+ ". I am "+age+ " years old. I am studying at "+institute+" in "+sub+". I am living at "+housing_name+". I would like to teach besides the " +
                            "area of "+housing_name+". I would like to teach class "+class_name+" students any subject specially "+subject+". If you want to engaged with me so please like me and then engage with me.");
                }catch (NullPointerException e){
                    e.printStackTrace();
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

                        likes.add(token);
                        final int counterss11=likes.size();
                        final String number11=String.valueOf(counterss11);
                        tutor_account_likes.setText(number11);
                    }catch (DatabaseException ignored){

                    }

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
                        engaged.add(informations);
                        final int counterss11111=engaged.size();
                        final String number1111=String.valueOf(counterss11111);
                        tutor_account_engage.setText(number1111);
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
                viewHolder.tv_mobile.setText(model.getMobile());
                viewHolder.tv_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_date_enagge.setText(model.getDate());
                viewHolder.tv_amount_engaged.setText(model.getAmount());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView_engage);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view, final int position, boolean isLongClick) {
                        try {
                            String key=adapter.getRef(position).getKey();
                            final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("user").child(key).child("info");
                            final DatabaseReference reference_remove=FirebaseDatabase.getInstance().getReference().child("Engaged").child(user_id).child(key);
                            final DatabaseReference reference_kkkk=FirebaseDatabase.getInstance().getReference().child("Engaged").child(user_id).child(key);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mobile_no= Objects.requireNonNull(dataSnapshot.child("mobile").getValue()).toString();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            engaged_dialog.setContentView(R.layout.engaged_dialog);
                            engaged_dialog.setCancelable(false);
                            final RelativeLayout layout__call=engaged_dialog.findViewById(R.id.call_now_tutor);
                            final RelativeLayout layout_remove=engaged_dialog.findViewById(R.id.remove_now_tutor);
                            layout_remove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                     reference_remove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if (task.isSuccessful()){
                                                reference_kkkk.child("state").setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Snackbar.make(scrollView,"Successfully removed form your engaged list",Snackbar.LENGTH_LONG).show();
                                                            engaged_dialog.dismiss();
                                                        }
                                                    }
                                                });


                                             }
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Snackbar.make(scrollView,"There have some problem please try again later",Snackbar.LENGTH_LONG).show();
                                         }
                                     });
                                }
                            });
                            final ImageView imageView=engaged_dialog.findViewById(R.id.imageView456);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    engaged_dialog.dismiss();
                                }
                            });
                            layout__call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onCallBtnClick();
                                    Toast.makeText(TutorAccountActivity.this,"Wait I am calling",Toast.LENGTH_LONG).show();
                                }
                            });
                            Objects.requireNonNull(engaged_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            engaged_dialog.show();

                        }catch (NullPointerException ignored){

                        }





                    }
                });
            }
        };
        recyclerView_engaged.setAdapter(adapter);

    }

    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {
            if (ActivityCompat.checkSelfPermission(TutorAccountActivity.this,
                    android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                ActivityCompat.requestPermissions(TutorAccountActivity.this, PERMISSIONS_STORAGE, 9);
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

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(TutorAccountActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mobile_no));
            TutorAccountActivity.this.startActivity(callIntent);

        }else{
            Snackbar.make(scrollView, "You don't assign permission.", Snackbar.LENGTH_LONG).show();
        }
    }
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    public void coment_tutor(View view) {


            Intent intent=new Intent(TutorAccountActivity.this,show_comment.class);
            intent.putExtra("key",user_id);
            startActivity(intent);

    }



//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    public void go_tutor_detail_activity(View view) {
        startActivity(new Intent(TutorAccountActivity.this,Home.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void alertdialog(View view) {
       dialog.setContentView(R.layout.tutor_account_setting);
       final ImageView imageView=dialog.findViewById(R.id.imageView_logout);
       final Button button_update=dialog.findViewById(R.id.button_update);
       final Button button_delete=dialog.findViewById(R.id.button_delete);
       final Button button_logout=dialog.findViewById(R.id.button_logout);
       button_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(TutorAccountActivity.this, R.style.MyDialogTheme);
               alertDialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Delete Account!!</font>"));
               alertDialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>Really you want to delete your account?</font>"));
               alertDialog.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, int arg1) {
                       dialog_dialog.show();
                       reference_female.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                  reference_male.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){
                                              reference_location.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()){
                                                         reference_porate.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 if (task.isSuccessful()){

                                                                                 dialog_dialog.dismiss();
                                                                                 dialog.dismiss();
                                                                                 Toast.makeText(TutorAccountActivity.this,"Your account deleted successfully",Toast.LENGTH_LONG).show();
                                                                                 startActivity(new Intent(TutorAccountActivity.this,Home.class));
                                                                                 finish();





                                                                 }
                                                             }
                                                         });

                                                     }
                                                  }
                                              });
                                          }
                                      }
                                  }) ;
                               }
                           }
                       });



                   }
               });
               alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int arg1) {
                       dialog.dismiss();

                   }
               });
               alertDialog.create();
               alertDialog.show();
           }
       });

       button_logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseAuth.getInstance().signOut();
               Toast.makeText(TutorAccountActivity.this,"You loged out",Toast.LENGTH_LONG).show();
               startActivity(new Intent(TutorAccountActivity.this,PermisionActivity.class));
               finish();
           }
       });
       button_update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(TutorAccountActivity.this,AddActivity.class);
               intent.putExtra("image_tutor",profile_image);
               intent.putExtra("name_tutor",name);
               startActivity(intent);
               dialog.dismiss();
               finish();
           }
       });
       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();

           }
       });
       Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialog.show();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
