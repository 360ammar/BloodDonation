package com.example.ammar.blooddonation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.ammar.blooddonation.Modals.UserProfile;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.io.IOException;

/**
 * Created by Ammar on 12/19/2017.
 */

public class PostVerification extends AppCompatActivity {

    ImageView profileImage;
    EditText nameText;
    EditText emailText;
    EditText phoneText;
    CountryCodePicker ccpSpin;
    Switch ablSwitch;
    EditText passwordText;
    private int PICK_IMAGE_REQUEST = 1;
    Uri uri;
    String _name;
    String phoneNum;
    Button Save;
    String TAG = "PostVerificationActivity";
    RadioButton femaleBtn;
    RadioButton maleBtn;
    Boolean gender;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileImage = (ImageView)findViewById(R.id.profileImage);
        passwordText = (EditText)findViewById(R.id.passwordText);
        nameText = (EditText)findViewById(R.id.nameText);
        emailText = (EditText)findViewById(R.id.emailText);
        phoneText = (EditText)findViewById(R.id.phoneText);
        ablSwitch = (Switch)findViewById(R.id.Switch);
        ccpSpin = (CountryCodePicker)findViewById(R.id.ccpSpin);
        Save = (Button)findViewById(R.id.btnSave);
        femaleBtn = (RadioButton)findViewById(R.id.femaleBtn);
        maleBtn = (RadioButton)findViewById(R.id.maleBtn);
        if(maleBtn.isChecked())
        {gender=true;}
        else{gender=false;}

       // _name = getIntent().getExtras().getString("name");
       // phoneNum = getIntent().getExtras().getString("phone");

        _name = "Ammar";
        phoneNum = "+923455449805";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d("Tag",phoneNum.substring(1,3));
        ccpSpin.setCountryForPhoneCode(Integer.parseInt(phoneNum.substring(1, 3)));
        nameText.setText(_name);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky is clicked
                //Toast.makeText(getApplicationContext(),"image clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfile uInfo = new UserProfile();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nameText.getText().toString())
                        .setPhotoUri(uri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }}
                        });
                if(emailText.getText().toString() != null){
                user.updateEmail(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });}
                user.updatePassword(passwordText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
                uInfo.setPhone(phoneNum);
                uInfo.setEmail(emailText.getText().toString());
                uInfo.setName(nameText.getText().toString());
                uInfo.setAvailibility(ablSwitch.isChecked());
                uInfo.setGender(gender);
                mDatabase.child("users").child(user.getUid()).setValue(uInfo);




            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            //  myPrefsEdit.putString("url", uri.toString());
            //  myPrefsEdit.commit();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
