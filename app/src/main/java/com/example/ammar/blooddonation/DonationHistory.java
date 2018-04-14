package com.example.ammar.blooddonation;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationHistory extends AppCompatActivity {
    private List<UserProfile> userData;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    FirebaseUser userID;
    Dialog rankDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(DonationHistory.this);
        rv.setLayoutManager(layoutManager);
        userData = new ArrayList<>();
        userID = FirebaseAuth.getInstance().getCurrentUser();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mDatabase.child("users").child(userID.getUid()).child("acceptedRecipients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final UserProfile Bl = new UserProfile();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Bl.setName(userSnapshot.child("name").getValue(String.class));
                        Bl.setPhone(userSnapshot.child("phone").getValue(String.class));
                        DatabaseReference ref = userSnapshot.getRef();
                        Bl.setDb(ref);
                        Bl.setUserID(userSnapshot.child("userID").getValue(String.class));
                        Log.d("data",Bl.getName() + ' ' + Bl.getBloodgroup() + ' ' + Bl.getAvailibility());
                        userData.add(new UserProfile(Bl.getName(), null, Bl.getPhone(),Bl.getDb(),Bl.getUserID()));
                    }
                    HistoryAdapter adapter1 = new HistoryAdapter(userData);
                  /*  istoryAdapter adapter1 = new HistoryAdapterH(getApplicationContext(), userData, new HistoryAdapter.CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            rankDialog = new Dialog(getApplicationContext(), R.style.FullHeightDialog);
                            rankDialog.setContentView(R.layout.rating_dialog);
                            rankDialog.setCancelable(true);
                            RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

                            ratingBar.setRating(Bl.getRating().floatValue());


                            TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                            text.setText(userData.get(position).getName());
                            Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                            updateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rankDialog.dismiss();
                                }
                            });
                            //now that the dialog is set up, it's time to show it
                            rankDialog.show();

                        }
                    }); */

                    rv.setAdapter(adapter1);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


}
