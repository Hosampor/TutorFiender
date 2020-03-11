package com.example.tutor.finder1;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Profile_image extends AppCompatActivity {
    DatabaseReference reference,reference_student_teacher;
    DatabaseReference button;

    DatabaseReference profile;
    CircleImageView imageView_profile;
    Button btn_image;

    public static final int GALERY_PIC=1;

    StorageReference storageReference;
    Toolbar toolbar_profile;

    RelativeLayout llllll;
    FirebaseUser user_main;
    String id2;
    String phone="";
    String type="";

    ProgressBar progressBar_upload;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        toolbar_profile=findViewById(R.id.profile_image);
        setSupportActionBar(toolbar_profile);
        user_main=FirebaseAuth.getInstance().getCurrentUser();
        assert user_main != null;
        id2=user_main.getUid();
        phone=getIntent().getStringExtra("phone");
        type=getIntent().getStringExtra("type");
        reference_student_teacher=FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child(type).child(phone);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Your Profile Image");
        toolbar_profile.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar_upload=findViewById(R.id.uploadBar);
        imageView_profile=findViewById(R.id.choose_profile_images);
        progressBar_upload.setVisibility(View.INVISIBLE);
        storageReference= FirebaseStorage.getInstance().getReference();
        profile=FirebaseDatabase.getInstance().getReference().child("user").child(id2).child("info");
        llllll=findViewById(R.id.profile_image_id);
        btn_image=findViewById(R.id.button3111);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALERY_PIC);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALERY_PIC&& resultCode==RESULT_OK){
            Uri imageuri=data.getData();
            CropImage.activity(imageuri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File thumb_file_path=new File(resultUri.getPath());
                Bitmap thumb_bitmap=new Compressor(this)
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .setQuality(75)
                        .compressToBitmap(thumb_file_path);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                final byte[] thum_byte=byteArrayOutputStream.toByteArray();
                final StorageReference thumb_filepath=storageReference.child("profile_image").child("thumbs").child(id2+".jpg");
                thumb_filepath.putBytes(thum_byte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        final String url=task.getResult().getDownloadUrl().toString();
                        profile.child("image").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   reference_student_teacher.child("image").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){
                                               startActivity(new Intent(Profile_image.this,PermisionActivity.class));
                                               finish();
                                           }
                                       }
                                   });

                               }
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar_upload.setVisibility(View.VISIBLE);

                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot

                                .getTotalByteCount());
                        int a=(int)progress;
                        progressBar_upload.setProgress(a);

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        profile.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image= Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                if (image.isEmpty()) {
                    imageView_profile.setImageResource(R.drawable.ic_action_avatar);
                } else{
                    Picasso.with(getBaseContext()).load(image).into(imageView_profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
