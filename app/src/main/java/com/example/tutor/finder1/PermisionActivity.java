package com.example.tutor.finder1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.tutor.finder1.Connectivity.CheckConnection;
import com.example.tutor.finder1.Helper.Student;
import com.example.tutor.finder1.Helper.UserInformation;
import com.example.tutor.finder1.PlaceHolderAdapter.PlaceAutocompleteAdapter;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class PermisionActivity extends AppCompatActivity {
    Dialog mydialog,authDialog;
    Button bt_signin,phone;
    FirebaseAuth auth;
    TextView textView;
    ImageView imageView;
    RelativeLayout layou;
    ProgressDialog progressDialog;
    ProgressDialog logindialog;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btntype=0;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    ViewFlipper viewFlipper;
    DatabaseReference reference;
    EditText emaillog,passlog,et_phone;
    TextView textView_jajaa;
    RelativeLayout midle_layout;
    DatabaseReference reference1111,reference_student_teacher,reference_student_teacher1,student_teacher,student_teacher1;
    DatabaseReference state_male1;
    String date;
    MaterialStyledDialog.Builder dialogHeader1;
    SpotsDialog mDialog;
    View mview;
    Spinner spinner_log;
    SimpleDateFormat df,df1;
    boolean doubleBackToExitPressedOnce = false;
    public PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    public GeoDataClient geoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(23.4905767, 88.0998169),
            new LatLng(23.4905767, 88.0998169));



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permision);



        date=new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Calendar.getInstance().getTime());




        progressDialog=new ProgressDialog(this);
        et_phone=findViewById(R.id.email_phone);
        spinner_log=findViewById(R.id.spin_log);
        emaillog=findViewById(R.id.email_home);
        passlog=findViewById(R.id.password_home);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Please wait a second");
        logindialog=new ProgressDialog(this);
        logindialog.setTitle("Logging In");
        logindialog.setMessage("Please wait we are logging you");
        layou=findViewById(R.id.rootlayout);
        auth=FirebaseAuth.getInstance();
        this.mydialog=new Dialog(PermisionActivity.this);
        this.authDialog=new Dialog(PermisionActivity.this);
        bt_signin=findViewById(R.id.button_home);
        textView_jajaa=findViewById(R.id.jajja);
        midle_layout=findViewById(R.id.relative_midle_section);
        textView=findViewById(R.id.texxxx);

        mDialog=new SpotsDialog(PermisionActivity.this);
        viewFlipper=findViewById(R.id.my_flipper);

        int image[]={R.drawable.aaaa,R.drawable.ccccc,R.drawable.ddddd};
        for (int imageaa:image){
            flipper_image(imageaa);

        }

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logindialog.show();
                String type=spinner_log.getSelectedItem().toString();
                if (TextUtils.isEmpty(type)){
                    Snackbar.make(layou,"Please enter email address",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    logindialog.dismiss();
                    return;
                }
                final String phone=et_phone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Snackbar.make(layou,"Please enter phone number",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    logindialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(emaillog.getText().toString())){
                    Snackbar.make(layou,"Please enter email address",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    logindialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(passlog.getText().toString())){
                    Snackbar.make(layou,"Please set a password",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    logindialog.dismiss();
                    return;
                }
                if (passlog.getText().toString().length()<6){
                    Snackbar.make(layou,"Password too short",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    logindialog.dismiss();
                    return;
                }
               student_teacher=FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child("Student");
               student_teacher1=FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child("Teacher");
                student_teacher.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.child(phone).exists()){
                           logindialog.dismiss();
                           Student student=dataSnapshot.child(phone).getValue(Student.class);
                           if (student.getPassword().equals(passlog.getText().toString())&&student.getEmail().equals(emaillog.getText().toString())){
                               auth.signInWithEmailAndPassword(emaillog.getText().toString(),passlog.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       if (task.isSuccessful()){
                                           progressDialog.dismiss();
                                           logindialog.dismiss();
                                           startActivity(new Intent(PermisionActivity.this,Home.class));
                                           finish();
                                       }
                                   }
                               });
                           }
                       }
                       //test......................................
                       else{
                           student_teacher1.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.child(phone).exists()){
                                       logindialog.dismiss();
                                       Student student=dataSnapshot.child(phone).getValue(Student.class);
                                       if (student.getPassword().equals(passlog.getText().toString())&&student.getEmail().equals(emaillog.getText().toString())){
                                           auth.signInWithEmailAndPassword(emaillog.getText().toString(),passlog.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if (task.isSuccessful()){
                                                       progressDialog.dismiss();
                                                       logindialog.dismiss();
                                                       startActivity(new Intent(PermisionActivity.this,Home_2.class));
                                                       finish();
                                                   }
                                               }
                                           });
                                       }

                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });

                       }
                       //test.....................................


                      // else {
                         //  progressDialog.dismiss();
                          // logindialog.dismiss();
                          // Toast.makeText(PermisionActivity.this,"Wrong email or password or phone",Toast.LENGTH_LONG).show();
                      // }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //Test

                //Test




            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(mydialog != null) {
            mydialog.dismiss();
            mydialog = null;
        }
    }
    public void flipper_image(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }





    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//           startActivity(new Intent(PermisionActivity.this,PermisionActivity.class));
//            finish();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckConnection.isNetworkAvaliable(this)){

        }else {
            final Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.internet);
            final Button button=dialog.findViewById(R.id.btn_retry);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckConnection.isNetworkAvaliable(PermisionActivity.this)){
                        dialog.dismiss();

                    }

                }
            });
            dialog.show();

        }
    }

    @SuppressLint("NewApi")
    public void showResisterlog(View view) {
        final LayoutInflater inflater=LayoutInflater.from(PermisionActivity.this);
        mview=inflater.inflate(R.layout.register,null);
        geoDataClient = Places.getGeoDataClient(this, null);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, geoDataClient, LAT_LNG_BOUNDS, null);
        final EditText name11=mview.findViewById(R.id.edit_name1111);
        final AutoCompleteTextView institute_name=mview.findViewById(R.id.edit_institute_name);
        final Spinner spinner_reg=mview.findViewById(R.id.spin_reg);
        institute_name.setAdapter(mPlaceAutocompleteAdapter);
        final EditText mobile=mview.findViewById(R.id.edit_mobile);
        final EditText email=mview.findViewById(R.id.edit_email1111);
        final EditText password=mview.findViewById(R.id.edit_password1111);
        final EditText dayes=mview.findViewById(R.id.edit_day);
        final EditText class_name=mview.findViewById(R.id.edit_class);
        final EditText confirm_password=mview.findViewById(R.id.edit_conpassword1111);


        dialogHeader1 = new MaterialStyledDialog.Builder(PermisionActivity.this)
                .setStyle(Style.HEADER_WITH_TITLE)
                .withDialogAnimation(true)
                .withDarkerOverlay(true)
                .setTitle("REGISTRATION FORM")
                .setHeaderColor(R.color.midle)
                //.setHeaderDrawable(R.drawable.background)
                .setPositiveText("REGISTER")
                .setScrollable(true)
                .setCustomView(mview,10,10,10,10)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog progressDialog, @NonNull DialogAction which) {
                        progressDialog.show();
                        final String type=spinner_reg.getSelectedItem().toString();
                        if (TextUtils.isEmpty(type)){
                            Snackbar.make(layou,"Please select register as above",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;

                        }
                        final String clas=class_name.getText().toString();
                        if (TextUtils.isEmpty(clas)){
                            Snackbar.make(layou,"Please enter class name",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }

                        final String name=name11.getText().toString();
                        if (TextUtils.isEmpty(name)){
                            Snackbar.make(layou,"Please enter your name",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        final String institutename=institute_name.getText().toString();
                        if (TextUtils.isEmpty(institutename)){
                            Snackbar.make(layou,"Please enter your institute name",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        final String mobileww= mobile.getText().toString();
                        if (TextUtils.isEmpty(mobileww)){
                            Snackbar.make(layou,"Please enter your mobile no",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        final String email1=email.getText().toString();
                        if (TextUtils.isEmpty(email1)){
                            Snackbar.make(layou,"Please enter email",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }

                        final String pass=password.getText().toString();
                        if (TextUtils.isEmpty(pass)){
                            Snackbar.make(layou,"Please set a password",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        final String confirm_pass=confirm_password.getText().toString();
                        if (!confirm_pass.equals(pass)){
                            Snackbar.make(layou,"Password not Match",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        final String days=dayes.getText().toString();
                        if (TextUtils.isEmpty(days)){
                            Snackbar.make(layou,"Please enter days",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        if (password.getText().toString().length()<6){
                            Snackbar.make(layou,"Password too short",Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        mDialog.show();
                        auth.createUserWithEmailAndPassword(email1,pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                            assert user != null;
                                            String id=user.getUid();
                                            date=new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Calendar.getInstance().getTime());
                                            state_male1 = FirebaseDatabase.getInstance().getReference().child("Button").child(id);
                                            reference1111= FirebaseDatabase.getInstance().getReference().child("user").child(id).child("info");
                                            reference_student_teacher=FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child(type).child(mobileww);
                                            //reference_student_teacher1=FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child("Teacher").child(mobileww);
                                            final String amount="Amount: Negotiable";
                                            final String image="null";
                                            UserInformation userInformation=new UserInformation(name,image,institutename,date,amount,mobileww);
                                            reference1111.setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        final Map map=new HashMap();
                                                        map.put("male","no");
                                                        map.put("female","no");
                                                        state_male1.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Student student=new Student(name,institutename,mobileww,days,date,image,email1,pass,clas);
                                                                    reference_student_teacher.setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
//                                                                                if(type=="Student")
//                                                                                {
//                                                                                    progressDialog.dismiss();
//                                                                                    mDialog.dismiss();
//                                                                                    finish();
//                                                                                    Intent intent=new Intent(PermisionActivity.this,Profile_image.class);
//                                                                                    intent.putExtra("phone",mobileww);
//                                                                                    intent.putExtra("type",type);
//                                                                                    Toast.makeText(PermisionActivity.this,"go Student",Toast.LENGTH_SHORT).show();
//                                                                                    startActivity(intent);
//                                                                                    finish();
//                                                                                }
//                                                                                else{
                                                                                    progressDialog.dismiss();
                                                                                    mDialog.dismiss();
                                                                                    finish();
                                                                                    Intent intent=new Intent(getApplicationContext(),Profile_image_teacher.class);
                                                                                    intent.putExtra("phone",mobileww);
                                                                                    intent.putExtra("type",type);
                                                                                    Toast.makeText(PermisionActivity.this,"Upload Picture",Toast.LENGTH_SHORT).show();
                                                                                    startActivity(intent);
                                                                                    finish();
//                                                                                }


                                                                            }

                                                                        }
                                                                    });





                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });




                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Snackbar.make(layou,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                });


                    }
                })
                .setNegativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        dialogHeader1.show();






    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void goTeacherList(View view) {
        startActivity(new Intent(PermisionActivity.this,TeacherList.class));

    }

    public void goStudentList(View view) {
        startActivity(new Intent(PermisionActivity.this,StudentList.class));
    }
}
