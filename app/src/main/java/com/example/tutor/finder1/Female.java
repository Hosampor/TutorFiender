package com.example.tutor.finder1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.tutor.finder1.Connectivity.CheckConnection;
import com.example.tutor.finder1.FemaleHolder.FemaleHolder;
import com.example.tutor.finder1.Helper.AllViewholder;
import com.example.tutor.finder1.Helper.Token;
import com.example.tutor.finder1.Interface.ItemClickListener;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Female extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    DrawerLayout drawer;
    MaterialSearchBar searchBar;
    RecyclerView recyclerView;
    LinearLayoutManager mlayoutManager;
    DatabaseReference reference_female,reference_folower;
    FirebaseRecyclerAdapter<AllViewholder,FemaleHolder> adapter;
    FirebaseRecyclerAdapter<AllViewholder,FemaleHolder> searchadapter;
    List<String> suggesstList=new ArrayList<>();
    RelativeLayout linearLayout;
    ProgressBar progressBar,progressBar_more;
    Button button;
    FirebaseUser user_tutor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female);
        drawer = findViewById(R.id.drawer_layout_female);
        linearLayout=findViewById(R.id.snack_female);
        NavigationView navigationView = findViewById(R.id.nav_view_female);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        recyclerView=findViewById(R.id.recyclerview_all_female);
        progressBar=findViewById(R.id.progressBar44_female);
        progressBar_more=findViewById(R.id.loadmore_female);
        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        mlayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlayoutManager);
        reference_female= FirebaseDatabase.getInstance().getReference().child("Female");
        reference_female.keepSynced(true);
        button=findViewById(R.id.reload_female);
        button.setVisibility(View.INVISIBLE);


        if (CheckConnection.isNetworkAvaliable(this)){
            load();
        }
        else {
            Snackbar.make(linearLayout,"No internet connection ",Snackbar.LENGTH_LONG).show();
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    load();
                    button.setVisibility(View.INVISIBLE);
                }
            });
        }

        searchBar = findViewById(R.id.searchbar_female);
        searchBar.setHint("Search by housing name");
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
                        drawer.openDrawer(Gravity.LEFT);
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

    private void loadsuggestion() {
        reference_female.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    AllViewholder item=postsnapshot.getValue(AllViewholder.class);
                    assert item != null;
                    suggesstList.add(item.getHousing_name());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void startsearch(CharSequence text) {
        searchadapter=new FirebaseRecyclerAdapter<AllViewholder, FemaleHolder>(

                AllViewholder.class,
                R.layout.female_adapter,
                FemaleHolder.class,
                reference_female.orderByChild("housing_name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(final FemaleHolder viewHolder, AllViewholder model, int position) {
                if (model.getImage().isEmpty()) {
                    viewHolder.imageView_female.setImageResource(R.drawable.nav_home_png);
                } else{
                    Picasso.with(getBaseContext()).load(model.getImage()).fit().centerCrop().into(viewHolder.imageView_female,new com.squareup.picasso.Callback(){

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
                viewHolder.tv_name.setText(model.getName());
                viewHolder.tv_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_subject.setText(model.getSubject());
                viewHolder.tv_gender.setText(model.getGender());
                viewHolder.tv_housing.setText(model.getHousing_name());
                viewHolder.setItemClickListener(new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        progressBar.setVisibility(View.VISIBLE);
                        final String tutor_key=searchadapter.getRef(position).getKey();
                        user_tutor=FirebaseAuth.getInstance().getCurrentUser();
                        assert user_tutor != null;
                        String uid=user_tutor.getUid();
                        Token token=new Token("yes","yes");
                        reference_folower=FirebaseDatabase.getInstance().getReference().child("Followers").child(tutor_key).child(uid).child("follower");
                        reference_folower.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent=new Intent(Female.this,Tutor_details_activity.class);
                                    intent.putExtra("taizul",tutor_key);
                                    intent.putExtra("home","female");
                                    startActivity(intent);
                                }
                            }
                        });


                    }
                });

            }
        };
        recyclerView.setAdapter(searchadapter);
    }



    private void load() {
        adapter=new FirebaseRecyclerAdapter<AllViewholder, FemaleHolder>(AllViewholder.class,
                R.layout.female_adapter,FemaleHolder.class,reference_female) {
            @Override
            protected void populateViewHolder(final FemaleHolder viewHolder, AllViewholder model, int position) {

                if (model.getImage().isEmpty()) {
                    viewHolder.imageView_female.setImageResource(R.drawable.nav_home_png);
                } else{
                    Picasso.with(getBaseContext()).load(model.getImage()).fit().centerCrop().into(viewHolder.imageView_female,new com.squareup.picasso.Callback(){

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
                viewHolder.tv_name.setText(model.getName());
                viewHolder.tv_institute_name.setText(model.getInstitute_name());
                viewHolder.tv_subject.setText(model.getSubject());
                viewHolder.tv_gender.setText(model.getGender());
                viewHolder.tv_housing.setText(model.getHousing_name());
                viewHolder.setItemClickListener(new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        progressBar.setVisibility(View.VISIBLE);
                        final String tutor_key=adapter.getRef(position).getKey();
                        user_tutor=FirebaseAuth.getInstance().getCurrentUser();
                        assert user_tutor != null;
                        String uid=user_tutor.getUid();
                        Token token=new Token("yes","yes");
                        reference_folower=FirebaseDatabase.getInstance().getReference().child("Followers").child(tutor_key).child(uid).child("follower");
                        reference_folower.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent=new Intent(Female.this,Tutor_details_activity.class);
                                    intent.putExtra("taizul",tutor_key);
                                    intent.putExtra("home","female");
                                    startActivity(intent);
                                }
                            }
                        });


                    }
                });






            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout_female);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView.scrollToPosition(preferences.getInt("position", 0));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollBy(0, - preferences.getInt("offset", 0));
            }
        }, 500);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            View firstChild = recyclerView.getChildAt(0);
            int firstVisiblePosition = recyclerView.getChildAdapterPosition(firstChild);
            int offset = firstChild.getTop();
            preferences.edit()
                    .putInt("position", firstVisiblePosition)
                    .putInt("offset", offset)
                    .apply();
        }catch (NullPointerException e){
           e.printStackTrace();
        }

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {


                Intent intent=new Intent(Female.this,AddActivity.class);
                startActivity(intent);

        }  else if (id == R.id.nav_share) {
            startActivity(new Intent(Female.this,Manage.class));

        } else if (id == R.id.malecvbn) {
            Intent intent=new Intent(Female.this,Male.class);

            startActivity(intent);
            finish();

        }else if (id==R.id.googlemap){

            Intent intent=new Intent(Female.this,MapsActivity.class);

            startActivity(intent);

        }
        else if (id == R.id.home_female_go_for_search) {
            Intent intent=new Intent(Female.this,Home.class);

            startActivity(intent);
            finish();

        }
        else if (id==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Female.this,PermisionActivity.class));
            finish();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout_female);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
