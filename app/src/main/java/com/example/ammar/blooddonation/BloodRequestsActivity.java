package com.example.ammar.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ammar.blooddonation.Modals.RequestforBlood;
import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BloodRequestsActivity extends AppCompatActivity {

    private List<RequestforBlood> bloodList;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    RVAdapter adapter1;
    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng latLng;
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(BloodRequestsActivity.this);
        //  layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        bloodList = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mapFragment = new MapFragment();
        mDatabase.child("recipient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RequestforBlood Bl = new RequestforBlood();
                    UserProfile uInfo = new UserProfile();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // final TransactionDetails tList = userSnapshot.getValue(TransactionDetails.class);
                        Bl.setBloodGroup(userSnapshot.child("bloodGroup").getValue(String.class));
                        Bl.setDaterequested(userSnapshot.child("date").getValue(Map.class));
                        Bl.setUserID(userSnapshot.child("userID").getValue(String.class));


                        Log.d("data",Bl.getBloodGroup() + ' ' + Bl.getDaterequested());
                        //  Transactionlist.setDate(Long.parseLong(userSnapshot.child("date").getValue(Map.class)));
                        bloodList.add(new RequestforBlood(Bl.getBloodGroup(),Bl.getDaterequested(),Bl.getUserID()));
                    }
                    adapter1 = new RVAdapter(bloodList);
                    rv.setAdapter(adapter1);

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MapFragment.MY_PERMISSIONS_REQUEST_LOCATION){
            mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_button,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        if (id == R.id.mybutton) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }


}
