package com.example.ammar.blooddonation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Ammar on 12/12/2017.
 */

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsVH>{

    static Context context;

    Dialog rankDialog;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser userID;


    public static class RequestsVH extends RecyclerView.ViewHolder {
        CardView cv;
        TextView _Message;
        Button ViewDetails;
        Button ShareDetails;



        RequestsVH(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            _Message = (TextView)itemView.findViewById(R.id.message);
            ViewDetails = (Button) itemView.findViewById(R.id.ViewDetails);
            ShareDetails = (Button) itemView.findViewById(R.id.ShareDetails);
        }
    }

    List<UserProfile> userData;

    RequestsAdapter(List<UserProfile> userData){
        this.userData = userData;
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
    @Override
    public RequestsVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history, viewGroup, false);
        RequestsVH pvh = new RequestsVH(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(RequestsVH UserViewHolder, final int i) {

        UserViewHolder._Message.setText(userData.get(i).getName() + " donated blood to you!");
        UserViewHolder.ViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent = intent.setData(Uri.parse("tel:" + userData.get(i).getPhone().trim()));
                context.startActivity(intent);

            }});
        UserViewHolder.ShareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Donor with " + userData.get(i).getBloodgroup() + " Blood Group Available!";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                context.startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });
        UserViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                rankDialog = new Dialog(context, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rating_dialog);
                rankDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                final DatabaseReference ref = userData.get(i).getDb();

                userID = FirebaseAuth.getInstance().getCurrentUser();
                //ratingBar.setRating(1);

                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(userData.get(i).getName());
                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                final Float[] recipientRating = new Float[1];
                ref.getRoot().child("users").child(userData.get(i).getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final UserProfile Bl = new UserProfile();
                            recipientRating[0] = dataSnapshot.child("donorRating").getValue(Float.class);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int m = 0;
                        if(recipientRating[0]==null)
                            recipientRating[0]=((float) m);
                        ref.child("rating").setValue(ratingBar.getRating());
                        ref.getRoot().child("users").child(userData.get(i).getUserID()).child("donorRating").setValue((recipientRating[0] + ratingBar.getRating())/2);
                        rankDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });  }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}