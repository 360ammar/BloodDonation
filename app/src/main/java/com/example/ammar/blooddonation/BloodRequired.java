package com.example.ammar.blooddonation;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.blooddonation.Modals.RequestforBlood;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class BloodRequired extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    Spinner spinner;
    EditText textUnits;
    EditText textCondition;
    Spinner SpinLevel;
    Button PickBtn;
    TextView textLocation;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser userID;
    String Units;
    String Level;
    String Location;
    String Condition;


    double lat;
    double lon;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder;
    private GoogleApiClient mGoogleApiClient;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_required);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textUnits = (EditText)findViewById(R.id.textUnits);
        textCondition = (EditText)findViewById(R.id.textCondition);
        SpinLevel = (Spinner)findViewById(R.id.SpinLevel);
        PickBtn = (Button)findViewById(R.id.PickBtn);
        textLocation = (TextView)findViewById(R.id.textLocation);



        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        spinner = (Spinner)findViewById(R.id.spinner2);
        String[] arraySpinner = new String[] {
                "A+", "O+", "B+", "AB+", "A-","O-","B-","AB-"
        };


        builder = new PlacePicker.IntentBuilder();

        fab = (FloatingActionButton) findViewById(R.id.fab);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(adapter);




        PickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivityForResult(builder.build(BloodRequired.this), PLACE_PICKER_REQUEST);
                    Toast.makeText(getApplicationContext(),"start activity for result",Toast.LENGTH_LONG).show();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(fab, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Location = place.getName().toString();
                textLocation.setText(Location);
            }
        }
    }

    public void RequestBlood(View view){



        Context context = getApplicationContext();
        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = mgr.getAllProviders();
            if (providers != null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
                Location loc = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    lat = loc.getLatitude();
                    lon = loc.getLongitude();
                }
            }
        }


        userID = FirebaseAuth.getInstance().getCurrentUser();
        RequestforBlood requestforBlood = new RequestforBlood();
        Map timestamp = ServerValue.TIMESTAMP;
        requestforBlood.setBloodGroup(spinner.getSelectedItem().toString());
        requestforBlood.setDaterequested(timestamp);
        requestforBlood.setUserID(userID.getUid());
        requestforBlood.setCondition(textCondition.getText().toString());
        requestforBlood.setLevelOfUrgency(SpinLevel.getSelectedItem().toString());
        requestforBlood.setUnits(parseInt(textUnits.getText().toString()));
        requestforBlood.setLocation(Location);
        requestforBlood.setLat(lat);
        requestforBlood.setLon(lon);
        DatabaseReference newDatabaseReference = mDatabase.child("recipient").push();
        String key =  newDatabaseReference.getKey();
        mDatabase.child("recipient").child(key).setValue(requestforBlood);
        Toast.makeText(BloodRequired.this,spinner.getSelectedItem().toString()+" Blood Requested!",Toast.LENGTH_LONG).show();



    }

}
