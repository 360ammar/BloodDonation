package com.example.ammar.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.ammar.blooddonation.Modals.UserProfile;

import java.util.List;

public class DonorsAdapter extends RecyclerView.Adapter<DonorsAdapter.UserVH>{

    static Context context;

    public static class UserVH extends RecyclerView.ViewHolder {
        CardView cv;
        TextView _Name;
        TextView _Message;
        Button ContactDonor;
        Button ShareDonor;


        UserVH(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            _Name = (TextView)itemView.findViewById(R.id.name);
            _Message = (TextView)itemView.findViewById(R.id.message);
            ContactDonor = (Button) itemView.findViewById(R.id.ContactDonor);
            ShareDonor = (Button) itemView.findViewById(R.id.ShareDonor);
        }
    }

    List<UserProfile> userData;

    DonorsAdapter(List<UserProfile> userData){
        this.userData = userData;
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
    @Override
    public UserVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_donors, viewGroup, false);
        UserVH pvh = new UserVH(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(UserVH UserViewHolder,final int i) {
        UserViewHolder._Name.setText(userData.get(i).getName());
        UserViewHolder._Message.setText(userData.get(i).getName() + " is available to donate " + userData.get(i).getBloodgroup() + " blood.");
        UserViewHolder.ContactDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent = intent.setData(Uri.parse("tel:" + userData.get(i).getPhone().trim()));
                context.startActivity(intent);
            }});
        UserViewHolder.ShareDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Donor with " + userData.get(i).getBloodgroup() + " Blood Group Available!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                context.startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
