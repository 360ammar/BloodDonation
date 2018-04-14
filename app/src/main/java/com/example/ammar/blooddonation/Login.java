package com.example.ammar.blooddonation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;


        public class Login extends AppCompatActivity {
            private static final String TAG = "LoginActivity";
            private static final int REQUEST_SIGNUP = 0;
            private FirebaseAuth mAuth;
            private FirebaseAuth.AuthStateListener mAuthListener;
EditText _emailText;
EditText _passwordText;
Button _loginButton;
TextView _signupLink;

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);


                _emailText = (EditText) findViewById(R.id.input_email);
                _passwordText =(EditText) findViewById(R.id.input_password);
                _loginButton = (Button) findViewById(R.id.btn_login);
                _signupLink = (TextView) findViewById(R.id.link_signup);

                mAuth = FirebaseAuth.getInstance();

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        } else {
                            // User is signed out
                            Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                        // ...
                    }
                };

                _loginButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        login();
                    }
                });

                _signupLink.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Start the Signup activity
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivityForResult(intent, REQUEST_SIGNUP);
                    }
                });
            }

            public void login() {
                Log.d(TAG, "Login");

                if (!validate()) {
                    onLoginFailed();
                    return;
                }

                _loginButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();

                // TODO: Implement your own authentication logic here.

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    onLoginSuccess();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    onLoginFailed();

                                }


                            }
                        });


            }


            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_SIGNUP) {
                    if (resultCode == RESULT_OK) {

                        // TODO: Implement successful signup logic here
                        // By default we just finish the Activity and log them in automatically
                        this.finish();
                    }
                }
            }
            @Override
            public void onStart() {
                super.onStart();
                mAuth.addAuthStateListener(mAuthListener);
            }
            @Override
            public void onStop() {
                super.onStop();
                if (mAuthListener != null) {
                    mAuth.removeAuthStateListener(mAuthListener);
                }
            }

            @Override
            public void onBackPressed() {
                // disable going back to the MainActivity
                moveTaskToBack(true);
            }

            public void onLoginSuccess() {
                _loginButton.setEnabled(true);
                startActivity(new Intent(Login.this,Dashboard.class));
                finish();
            }

            public void onLoginFailed() {
                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

                _loginButton.setEnabled(true);
            }

            public boolean validate() {
                boolean valid = true;

                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    _emailText.setError("enter a valid email address");
                    valid = false;
                } else {
                    _emailText.setError(null);
                }

                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    _passwordText.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {_passwordText.setError(null);}return valid;}}