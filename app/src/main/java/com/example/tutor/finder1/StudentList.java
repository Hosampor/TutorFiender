package com.example.tutor.finder1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tutor.finder1.Connectivity.CheckConnection;
import com.example.tutor.finder1.FemaleHolder.StudentHolder;
import com.example.tutor.finder1.Helper.AllViewholder;
import com.example.tutor.finder1.Helper.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends AppCompatActivity {
    DrawerLayout drawer;
    MaterialSearchBar searchBar;
    RecyclerView recyclerView;
    LinearLayoutManager mlayoutManager;
    DatabaseReference reference,reference_folower;
    FirebaseRecyclerAdapter<Student,StudentHolder> adapter;
    FirebaseRecyclerAdapter<Student,StudentHolder> searchadapter;
    List<String> suggesstList=new ArrayList<>();
    ProgressBar progressBar,progressBar_more;
    Button button;
    FirebaseUser user_tutor;
    boolean doubleBackToExitPressedOnce = false;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);



        recyclerView=findViewById(R.id.recyclerview_all_st);





        mlayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mlayoutManager);

        reference= FirebaseDatabase.getInstance().getReference().child("AllUserAccess").child("Student");

        reference.keepSynced(true);

        imageView=findViewById(R.id.back_imageview_st);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentList.this,PermisionActivity.class));
                finish();
            }
        });



        if (CheckConnection.isNetworkAvaliable(this)){
            load();
        }
        else {
            //Snackbar.make(linearLayout,"No internet connection ",Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();

        }

        searchBar = findViewById(R.id.searchBar_st);
        searchBar.setHint("Search by Institute Name");
        searchBar.setCardViewElevation(10);
        loadsuggestion();
        searchBar.setLastSuggestions(suggesstList);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest=new ArrayList<>();
                for (String search:suggesstList){
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startsearch(text);
            }

            @SuppressLint("RtlHardcoded")
            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode){
                    case MaterialSearchBar.BUTTON_NAVIGATION:

                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        searchBar.disableSearch();
                        break;
                }


            }
        });

    }

    private void startsearch(CharSequence text) {
        searchadapter=new FirebaseRecyclerAdapter<Student, StudentHolder>(Student.class,
                R.layout.student_teacher_adapter,
                StudentHolder.class,
                reference.orderByChild("institute_name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(final StudentHolder viewHolder, Student model, int position) {
                if (model.getImage().isEmpty()) {
                    viewHolder.circleImageView.setImageResource(R.drawable.nav_home_png);
                } else{
                    Picasso.with(getBaseContext()).load(model.getImage()).fit().centerCrop().into(viewHolder.circleImageView,new com.squareup.picasso.Callback(){

                        @Override
                        public void onSuccess() {
                            if (viewHolder.progressBar!=null){
                                viewHolder.progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                viewHolder.tv_st_name.setText(model.getName());
                viewHolder.tv_st_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_st_class.setText(model.getClass_name());
                viewHolder.tv_st_days.setText(model.getDays());
                viewHolder.tv_st_ago.setText(model.getTime());



            }
        };
        recyclerView.setAdapter(searchadapter);

    }

    private void loadsuggestion() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    AllViewholder allViewholder=postsnapshot.getValue(AllViewholder.class);
                    assert allViewholder != null;
                    suggesstList.add(allViewholder.getHousing_name());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void load() {

        adapter=new FirebaseRecyclerAdapter<Student, StudentHolder>(Student.class,
                R.layout.student_teacher_adapter,StudentHolder.class,reference) {
            @Override
            protected void populateViewHolder(final StudentHolder viewHolder, Student model, int position) {

                if (model.getImage().isEmpty()) {
                    viewHolder.circleImageView.setImageResource(R.drawable.nav_home_png);
                } else{
                    Picasso.with(getBaseContext()).load(model.getImage()).fit().centerCrop().into(viewHolder.circleImageView,new com.squareup.picasso.Callback(){

                        @Override
                        public void onSuccess() {
                            if (viewHolder.progressBar!=null){
                                viewHolder.progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                viewHolder.tv_st_name.setText(model.getName());
                viewHolder.tv_st_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_st_class.setText(model.getClass_name());
                viewHolder.tv_st_days.setText(model.getDays());
                viewHolder.tv_st_ago.setText(model.getTime());




            }
        };
        recyclerView.setAdapter(adapter);
    }
}
