package com.example.ammar.blooddonation;


import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableDonorsActivity extends AppCompatActivity{
    private List<UserProfile> userData;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    DonorsMapFragment mapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_donors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv_donors);

        mapFragment = new DonorsMapFragment();
        layoutManager = new LinearLayoutManager(AvailableDonorsActivity.this);
        //  layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        userData = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserProfile Bl = new UserProfile();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // final TransactionDetails tList = userSnapshot.getValue(TransactionDetails.class);
                        Bl.setName(userSnapshot.child("name").getValue(String.class));
                        Bl.setBloodgroup(userSnapshot.child("bloodgroup").getValue(String.class));
                        Bl.setAvailibility(userSnapshot.child("availibility").getValue(Boolean.class));
                        Bl.setPhone(userSnapshot.child("phone").getValue(String.class));

                        Log.d("data",Bl.getName() + ' ' + Bl.getBloodgroup() + ' ' + Bl.getAvailibility());

                        //  Transactionlist.setDate(Long.parseLong(userSnapshot.child("date").getValue(Map.class)));
                        if(Bl.getAvailibility()) {
                            userData.add(new UserProfile(Bl.getName(), Bl.getBloodgroup(), Bl.getPhone(),null,null));
                        }
                    }
                    final DonorsAdapter adapter1 = new DonorsAdapter(userData);
                    rv.setAdapter(adapter1);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




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
