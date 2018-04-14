package com.example.ammar.blooddonation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.hbb20.CountryCodePicker;

import java.io.IOException;

public class ProfilePage extends AppCompatActivity {

    ImageView profileImage;
    EditText nameText;
    EditText emailText;
    EditText phoneText;
    CountryCodePicker ccpSpin;
    Switch ablSwitch;
    EditText passwordText;
    private int PICK_IMAGE_REQUEST = 1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        profileImage = (ImageView)findViewById(R.id.profileImage);
        passwordText = (EditText)findViewById(R.id.passwordText);
        nameText = (EditText)findViewById(R.id.nameText);
        emailText = (EditText)findViewById(R.id.emailText);
        phoneText = (EditText)findViewById(R.id.phoneText);
        ablSwitch = (Switch)findViewById(R.id.Switch);
        ccpSpin = (CountryCodePicker)findViewById(R.id.ccpSpin);

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
