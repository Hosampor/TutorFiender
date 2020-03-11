package com.example.tutor.finder1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.tutor.finder1.Helper.AllViewholder;
import com.example.tutor.finder1.Helper.LocationViewholder;
import com.example.tutor.finder1.PlaceHolderAdapter.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class AddActivityTeacher extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {
    MaterialEditText et_lat, et_long,  et_age, et_subject_name, et_interested_area,
            et_interested_class, et_interested_subject,et_email_no;
    AutoCompleteTextView et_housing_nam;
    Spinner spinner,spin_Subject;
    Button button;
    Button submitButton;
    FirebaseAuth addAuth;
    FirebaseUser user;
    DatabaseReference reference,reference_location,reference_male;
    ProgressDialog dialog;
    Toolbar toolbar;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    LocationManager mLocationManager;
    LocationRequest mLocationRequest;
    long UPDATE_INTERVAL = 2 * 1000;
    long FASTEST_INTERVAL = 2000;
    LocationManager locationManager;
    public PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    public GeoDataClient geoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(23.4905767, 88.0998169),
            new LatLng(23.4905767, 88.0998169));
    RelativeLayout lll;
    DatabaseReference profile_user;
    String image_user="";
    String name_user="";
    String institute_name_user="";
    String mobile="";
    DatabaseReference state_button;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        dialog = new ProgressDialog(this);
        et_lat = findViewById(R.id.edit_latitute);
        et_long = findViewById(R.id.edit_longitute);
        et_age = findViewById(R.id.edit_addage);
        et_subject_name = findViewById(R.id.edit_addsubject);
        et_interested_area = findViewById(R.id.edit_interested_area);
        et_interested_class = findViewById(R.id.edit_interested_class);
        et_email_no=findViewById(R.id.valid_email);
        toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        lll = findViewById(R.id.add_poratechai_layut);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_interested_subject = findViewById(R.id.edit_interested_subject);
        geoDataClient = Places.getGeoDataClient(this, null);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, geoDataClient, LAT_LNG_BOUNDS, null);
        et_housing_nam = findViewById(R.id.edit_housingName);
        et_housing_nam.setAdapter(mPlaceAutocompleteAdapter);
        spinner = findViewById(R.id.spin);
        spin_Subject=findViewById(R.id.spin1);

        submitButton = findViewById(R.id.save_now);
        addAuth = FirebaseAuth.getInstance();
        user = addAuth.getCurrentUser();
        assert user != null;
        final String uid = user.getUid();
        profile_user=FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("info");
        state_button = FirebaseDatabase.getInstance().getReference().child("Button").child(uid);
        profile_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    image_user=dataSnapshot.child("image").getValue().toString();
                    name_user=dataSnapshot.child("name").getValue().toString();
                    institute_name_user=dataSnapshot.child("institute_name").getValue().toString();
                    mobile=dataSnapshot.child("mobile").getValue().toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("poratechai").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String interested_area= Objects.requireNonNull(dataSnapshot.child("interested_area").getValue()).toString();
                    String email= Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                    String contact= Objects.requireNonNull(dataSnapshot.child("contact").getValue()).toString();
                    String age= Objects.requireNonNull(dataSnapshot.child("age").getValue()).toString();
                    String institute= Objects.requireNonNull(dataSnapshot.child("institute_name").getValue()).toString();
                    String sub= Objects.requireNonNull(dataSnapshot.child("subject").getValue()).toString();
                    String housing_name= Objects.requireNonNull(dataSnapshot.child("housing_name").getValue()).toString();
                    String class_name= Objects.requireNonNull(dataSnapshot.child("interested_class").getValue()).toString();
                    String subject= Objects.requireNonNull(dataSnapshot.child("interested_subject").getValue()).toString();
                    et_interested_area.setText(interested_area);

                    et_age.setText(age);
                    et_subject_name.setText(sub);
                    et_interested_class.setText(class_name);
                    et_interested_subject.setText(subject);
                    et_housing_nam.setText(housing_name);

                    et_email_no.setText(email);






                }catch (NullPointerException ignored){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference_location = FirebaseDatabase.getInstance().getReference().child("location").child(uid);
        reference_male = FirebaseDatabase.getInstance().getReference();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.setTitle("Saving...");
                dialog.setMessage("Please wait a moment");
                dialog.show();
                final String gender = spinner.getSelectedItem().toString();
                String p = "Select One";
                if (p.equals(gender)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please select one", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String subject = spin_Subject.getSelectedItem().toString();
                if (TextUtils.isEmpty(subject)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter your subject name", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String housing = et_housing_nam.getText().toString();
                if (TextUtils.isEmpty(housing)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter your housing name", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String age = et_age.getText().toString();
                if (TextUtils.isEmpty(age)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter your age", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String area = et_interested_area.getText().toString();
                if (TextUtils.isEmpty(area)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter intersted area", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String interestedclass = et_interested_class.getText().toString();
                if (TextUtils.isEmpty(interestedclass)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter interested classes", Snackbar.LENGTH_LONG).show();
                    return;
                }
                final String interested_sub = et_interested_subject.getText().toString();
                if (TextUtils.isEmpty(interested_sub)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Please enter intetested subject", Snackbar.LENGTH_LONG).show();
                    return;
                }

                final String email = et_email_no.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    dialog.dismiss();
                    Snackbar.make(lll, "Plese enter your valid email", Snackbar.LENGTH_LONG).show();
                    return;
                }


                final double latitute = Double.parseDouble(et_lat.getText().toString());
                final double longitude = Double.parseDouble(et_long.getText().toString());
                final AllViewholder allViewholder = new AllViewholder(name_user, institute_name_user, subject, gender, age, area, interestedclass, interested_sub, housing, mobile,email, image_user);
                reference.setValue(allViewholder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            reference_male.child(gender).child(uid).setValue(allViewholder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        LocationViewholder locationViewholder = new LocationViewholder(name_user, institute_name_user, uid, latitute, longitude);
                                        reference_location.setValue(locationViewholder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    String p1 = "Male";
                                                    String p2 = "Female";
                                                    if (p1.equals(gender)){
                                                        state_button.child("male").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    dialog.dismiss();
                                                                    Intent intent=new Intent(AddActivityTeacher.this,Home_2.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        }) ;
                                                    }else if (p2.equals(gender)){
                                                        state_button.child("female").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    dialog.dismiss();
                                                                    Intent intent=new Intent(AddActivityTeacher.this,Home_2.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                    }
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
        });

    }
    private void checkLocation() {
        if (!isLocationEnabled()){
            showAlert();
        }

        //isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivityTeacher.this);
        dialog.setTitle("Enable Location");
        dialog.setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                "use this app");
        dialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                dialog.setCancelable(true);
                paramDialogInterface.dismiss();
                System.exit(0);
            }
        });
        dialog.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }



    }

    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        et_lat.setText(String.valueOf(location.getLatitude()));
        et_long.setText(String.valueOf(location.getLongitude() ));



    }
}
