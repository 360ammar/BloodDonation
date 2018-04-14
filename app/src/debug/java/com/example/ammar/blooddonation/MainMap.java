package com.example.ammar.blooddonation;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ammar.blooddonation.Modals.RequestforBlood;
import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    LatLng latLng;
    Random r;
    Geocoder coder;
    List<Address> address;
    List<Marker> busesList;
    List<Marker> trainsList;
    HashMap<String,Marker> RecipientList;
    HashMap<String,Marker> DonorList;
    Spinner Spinner4;
    Spinner Spinner5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        busesList = new ArrayList<Marker>();
        trainsList = new ArrayList<Marker>();
        RecipientList = new HashMap<String,Marker>();
        DonorList     = new HashMap<String,Marker>();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
           .findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);
    }




    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainMap.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);

                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserProfile Bl = new UserProfile();
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // final TransactionDetails tList = userSnapshot.getValue(TransactionDetails.class);
                                //Bl.setBloodGroup(userSnapshot.child("bloodGroup").getValue(String.class));
                                Bl.setName(userSnapshot.child("name").getValue(String.class));
                                Bl.setBloodgroup(userSnapshot.child("bloodgroup").getValue(String.class));
                                Bl.setAvailibility(userSnapshot.child("availibility").getValue(Boolean.class));
                                Bl.setCity(userSnapshot.child("city").getValue(String.class));

                                // Log.d("data",Bl.getBloodGroup() + ' ' + Bl.getDaterequested());
                                //  Transactionlist.setDate(Long.parseLong(userSnapshot.child("date").getValue(Map.class)));
                                //bloodList.add(new RequestforBlood(Bl.getBloodGroup(),Bl.getDaterequested()));




                                coder = new Geocoder(getApplicationContext());

                                try {

                                    address = coder.getFromLocationName(Bl.getCity(), 5);
                                    android.location.Address location = address.get(0);
                                    location.getLatitude();
                                    location.getLongitude();
                                    if(address != null) {
                                        r = new Random();
                                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        markerOptions.title("Donor with " + Bl.getBloodgroup() + " Bloodgroup");
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                                       // busesList.add(mCurrLocationMarker);
                                        RecipientList.put(Bl.getBloodgroup(),mCurrLocationMarker);
                                      //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

                                    }

                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabase.child("recipient").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            RequestforBlood Bl = new RequestforBlood();
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // final TransactionDetails tList = userSnapshot.getValue(TransactionDetails.class);
                                //Bl.setBloodGroup(userSnapshot.child("bloodGroup").getValue(String.class));
                                Bl.setLat(userSnapshot.child("lat").getValue(Double.class));
                                Bl.setLon(userSnapshot.child("lon").getValue(Double.class));

                                Log.d("data",Bl.getBloodGroup() + ' ' + Bl.getDaterequested());
                                //  Transactionlist.setDate(Long.parseLong(userSnapshot.child("date").getValue(Map.class)));
                                //bloodList.add(new RequestforBlood(Bl.getBloodGroup(),Bl.getDaterequested()));


                                if(Bl.getLat() != null) {
                                    r = new Random();
                                    Bl.setBloodGroup(userSnapshot.child("bloodGroup").getValue(String.class));
                                    double Cords[] = getAddressFromLocation(Bl.getLat()+r.nextInt(1001) / 1000, Bl.getLon()+r.nextInt(1001) / 1000);
                                    //    latLng = new LatLng(Bl.getLat()+r.nextInt(1001) / 1000, Bl.getLon()+r.nextInt(1001) / 1000);
                                    latLng = new LatLng(Cords[0],Cords[1]);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title(Bl.getBloodGroup() + " Bloodgroup Required!");
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                                    trainsList.add(mCurrLocationMarker);
                                    DonorList.put(Bl.getBloodGroup(),mCurrLocationMarker);

                                }
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Place current location marker


                //add others like this




            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainMap.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private double[] getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(getApplicationContext());
        double lat = 0;
        double lon = 0;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("Address", addresses.toString());
            Address  fetchedAddress = addresses.get(0);
            addresses = geocoder.getFromLocationName(fetchedAddress.getAddressLine(0), 5);
            android.location.Address location = addresses.get(0);
            lat = location.getLatitude();
            lon = location.getLongitude();

            Log.d("Full Address", fetchedAddress.getAddressLine(0) + "Lat = " + lat+ " Lon= " + lon);
            // Log.d("TextAdd" ,add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[] {lat, lon};
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(MainMap.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
    }



    public void onConnectionSuspended(int i) {}


    public void onConnectionFailed(ConnectionResult connectionResult) {}


    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

/*
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
*/
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainMap.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainMap.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(MainMap.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainMap.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainMap.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(MainMap.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainMap.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    AlertDialog dialog;
    CheckBox buses, trains;

    public void filterTheMarkers(View view) {
        if (dialog == null) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View checkBoxView = inflater.inflate(R.layout.custom_dialog, null);
            builder.setView(checkBoxView);
            Spinner4 = (Spinner) checkBoxView.findViewById(R.id.spinner4);
            Spinner5 = (Spinner) checkBoxView.findViewById(R.id.spinner5);

            String[] arraySpinner = new String[] {
                    "A+", "O+", "B+", "AB+", "A-","O-","B-","AB-"
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainMap.this, android.R.layout.simple_spinner_item, arraySpinner);
            Spinner4.setAdapter(adapter);
            Spinner5.setAdapter(adapter);
            buses = (CheckBox) checkBoxView.findViewById(R.id.checkBox);
            trains = (CheckBox) checkBoxView.findViewById(R.id.checkBox2);
            //Button okButton = (Button) checkBoxView.findViewById(R.id.okButton);
            //Button cancelButton = (Button) checkBoxView.findViewById(R.id.cancelButton);
            dialog = builder.create();
        }
        dialog.show();
    }

    public void displaySelectedMarkers(View view) {
        dialog.dismiss();
        Log.i("Train Data", trainsList.get(0).toString());
        Log.i("TAG", "Trains Status " + trains.isChecked() + " Bus Status  " + buses.isChecked());
        //according these check boxes status execute your code to show/hide markers


        for(Map.Entry<String, Marker> entry : RecipientList.entrySet()) {
            String key = entry.getKey();
            Marker value = entry.getValue();
            value.setVisible(trains.isChecked());
            if(key.equals(Spinner4.getSelectedItem().toString()))
            {value.setVisible(true);}
            else{ value.setVisible(false); }
        }

        for(Map.Entry<String, Marker> entry : DonorList.entrySet()) {
            String key = entry.getKey();
            Marker value = entry.getValue();
            value.setVisible(buses.isChecked());
            if(key.equals(Spinner5.getSelectedItem().toString()))
            {value.setVisible(true);}
            else{value.setVisible(false);}
        }

        for(Marker train : trainsList){
            train.setVisible(trains.isChecked());
            Log.i("Train Data", trainsList.get(1).toString());
        }
        for(Marker buss : busesList){
            buss.setVisible(buses.isChecked());
        }
    }

    public void doNothing(View view) { dialog.dismiss(); }

}
