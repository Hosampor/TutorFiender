package com.example.tutor.finder1;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;

import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import android.widget.Toast;


import com.example.tutor.finder1.Helper.LocationViewholder;
import com.example.tutor.finder1.Helper.Token;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import me.biubiubiu.justifytext.library.JustifyTextView;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {
    private static final int ERRORDIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public boolean mLocationPermissionGranted = false;
    GoogleMap mMap;
    DatabaseReference reference;
    Marker marker;
    JustifyTextView name;
    DatabaseReference reference_folower;
    FirebaseUser user_user;




    private static final float DEFAULT_FLOAT = 17f;
    public FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        reference = FirebaseDatabase.getInstance().getReference().child("location");
        user_user= FirebaseAuth.getInstance().getCurrentUser();
        reference.push().setValue(marker);
        if (isServiceOk()) {
            getLOcationPermission();

        }

    }

    private void initmap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnInfoWindowClickListener(this);
            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {
                        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.info_window, null);
                        name = view.findViewById(R.id.map_name);


                        name.setText(marker.getTitle());
                        return view;
                    }
                });
            }
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {




                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        LocationViewholder viewholder = s.getValue(LocationViewholder.class);
                        assert viewholder != null;
                        LatLng latLng = new LatLng(viewholder.getLatitude(), viewholder.getLongitude());

                            setMarker(viewholder, latLng);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }





    }

    private void setMarker(LocationViewholder viewholder, LatLng latLng) {


        MarkerOptions options = new MarkerOptions()
                .title(viewholder.getUser_name()+"\n"+viewholder.getInstitute_name())
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet(viewholder.getUniquekey());


        marker = mMap.addMarker(options);

    }
    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentlocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()), DEFAULT_FLOAT,"My Location");

                        } else {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private void moveCamera(LatLng latLng, float zoom,String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!title.equals("My Location")){
            MarkerOptions options=new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }


    }







    @Override
    public void onInfoWindowClick(Marker marker) {
        String uid=user_user.getUid();
        Token token=new Token("jjjjj","jjjj");
        final String tutor_key=marker.getSnippet();
        reference_folower=FirebaseDatabase.getInstance().getReference().child("Followers").child(tutor_key).child(uid).child("follower");
        reference_folower.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent=new Intent(MapsActivity.this,Tutor_details_activity.class);
                    intent.putExtra("taizul",tutor_key);
                    intent.putExtra("home","map");
                    startActivity(intent);
                }
            }
        });









    }
    private void getLOcationPermission() {
        String[] permission = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initmap();
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initmap();

                }
            }

        }
    }
    public boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MapsActivity.this, available, ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make request", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}
