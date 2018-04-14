package com.example.ammar.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.blooddonation.Modals.RequestforBlood;
import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BloodVH>{

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static Context context;
    FirebaseUser userID;


    public static class BloodVH extends RecyclerView.ViewHolder {
        CardView cv;
        TextView Message;
        TextView Date;
        Button AcceptRequest;
        Button RejectRequest;
        Button ShareRequest;
        BloodVH(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            Message = (TextView)itemView.findViewById(R.id.message);
            Date = (TextView)itemView.findViewById(R.id.date);
            AcceptRequest = (Button) itemView.findViewById(R.id.AcceptRequest);
            RejectRequest = (Button) itemView.findViewById(R.id.RejectRequest);
            ShareRequest = (Button) itemView.findViewById(R.id.ShareRequest);

        }
    }

    public List<RequestforBlood> bloodList;
    RVAdapter(List<RequestforBlood> bloodList){
        this.bloodList = bloodList;
    }

    @Override
    public int getItemCount() {
        return bloodList.size();
    }
    @Override
    public BloodVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View  v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list, viewGroup, false);
        BloodVH pvh = new BloodVH(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(final BloodVH personViewHolder, final int i) {

            personViewHolder.Message.setText(bloodList.get(i).getBloodGroup() + " Blood required contact to donate blood!");
            personViewHolder.Date.setText("123");
            personViewHolder.AcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

               // Toast.makeText(context,bloodList.get(i).getUserID(), Toast.LENGTH_SHORT).show();
                userID = FirebaseAuth.getInstance().getCurrentUser();
                final UserProfile uInfo = new UserProfile();
                RequestforBlood Bl = new RequestforBlood();
                mDatabase.child("users").child(userID.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            uInfo.setPhone(dataSnapshot.child("phone").getValue(String.class));
                            uInfo.setName(dataSnapshot.child("name").getValue(String.class));
                            uInfo.setEmail(dataSnapshot.child("email").getValue(String.class));
                            uInfo.setBloodgroup(dataSnapshot.child("bloodgroup").getValue(String.class));
                            String UserID = userID.getUid();

                            DatabaseReference newDatabaseReference =  mDatabase.child("users").child(bloodList.get(i).getUserID()).child("acceptedDonors").push();
                            String uKey =  newDatabaseReference.getKey();
                            //String uKey = mDatabase.child("users").child(bloodList.get(i).getUserID()).getKey();
                            mDatabase.child("users").child(bloodList.get(i).getUserID()).child("acceptedDonors").child(uKey).setValue(uInfo);
                            mDatabase.child("users").child(bloodList.get(i).getUserID()).child("acceptedDonors").child(uKey).child("userID").setValue(UserID);

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Log.d("TAG",bloodList.get(i).getUserID());
                mDatabase.child("users").child(bloodList.get(i).getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {


                            uInfo.setPhone(dataSnapshot.child("phone").getValue(String.class));
                            uInfo.setName(dataSnapshot.child("name").getValue(String.class));
                            uInfo.setEmail(dataSnapshot.child("email").getValue(String.class));
                            uInfo.setBloodgroup(dataSnapshot.child("bloodgroup").getValue(String.class));
                            DatabaseReference ref = dataSnapshot.getRef();


                            Log.d("Tag",uInfo.getPhone());
                            String UserID = bloodList.get(i).getUserID();
                            DatabaseReference newDatabaseReference =  mDatabase.child("users").child(userID.getUid()).child("acceptedRecipients").push();
                            String Key =  newDatabaseReference.getKey();
                            mDatabase.child("users").child(userID.getUid()).child("acceptedRecipients").child(Key).setValue(uInfo);
                            mDatabase.child("users").child(userID.getUid()).child("acceptedRecipients").child(Key).child("userID").setValue(UserID);
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + uInfo.getPhone().trim()));
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        personViewHolder.RejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                removeItem(personViewHolder.getAdapterPosition());
            }
        });
        personViewHolder.ShareRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = bloodList.get(i).getBloodGroup() + "Blood Required!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                context.startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });
    }

    private void removeItem(int position) {
        bloodList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bloodList.size());
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
