package com.brainiac.mocae.needhelp;

import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class
MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProfilePictureView profilePictureView;
    private Button OfferHelpButton;
    private Button AskHelpButton;
    private TextView AlertLogInTxtView;
    private Button UpcomingEvents;
    private Button CreatedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OfferHelpButton = (Button) findViewById(R.id.offerHelpButton);
        AskHelpButton = (Button) findViewById(R.id.askForHelpButton);
        AlertLogInTxtView = (TextView) findViewById(R.id.alertLogInText);
        UpcomingEvents = (Button) findViewById(R.id.myUpcommingButton);
        CreatedEvents = (Button) findViewById(R.id.myCreatedButton);

        mAuth = FirebaseAuth.getInstance();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureView);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && Profile.getCurrentProfile() != null) {
                    // User is signed in
                    DataStorage.getInstance().getJoinedEventsIds();
                    profilePictureView.setVisibility(View.VISIBLE);
                    Profile profile = Profile.getCurrentProfile();
                    profilePictureView.setProfileId(profile.getId());
                    DataStorage.getInstance().SetCurrentUser(user.getUid());
                    Log.d("NeedHelp", "onAuthStateChanged:signed_in:" + profile.getId());
                    OfferHelpButton.setVisibility(View.VISIBLE);
                    AskHelpButton.setVisibility(View.VISIBLE);
                    UpcomingEvents.setVisibility(View.VISIBLE);
                    CreatedEvents.setVisibility(View.VISIBLE);
                    AlertLogInTxtView.setVisibility(View.GONE);
                } else {
                    // User is signed out
                    Profile.setCurrentProfile(null);
                    profilePictureView.setProfileId(null);
                    profilePictureView.setVisibility(View.INVISIBLE);
                    DataStorage.getInstance().Reset();
                    Log.d("NeedHelp", "onAuthStateChanged:signed_out");
                    OfferHelpButton.setVisibility(View.GONE);
                    AskHelpButton.setVisibility(View.GONE);
                    UpcomingEvents.setVisibility(View.GONE);
                    CreatedEvents.setVisibility(View.GONE);
                    AlertLogInTxtView.setVisibility(View.VISIBLE);
                }
                // ...
            }
        };

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("NeedHelp", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("NeedHelp", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("NeedHelp", "facebook:onError", error);
                // ...
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NeedHelp", "loginOnClick");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Profile.setCurrentProfile(null);
                    profilePictureView.setProfileId(null);
                    profilePictureView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("NeedHelp", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("NeedHelp", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("NeedHelp", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    public void onOfferHelpClick(View view) {
        Intent intent = new Intent (this, OfferHelpActivity.class);
        startActivity(intent);
    }

    public void onAskHelpClick(View view) {
        Intent intent = new Intent (this, AskHelpActivity.class);
        startActivity(intent);
    }

    public void onUpcomingEventsClick(View view) {
        Intent intent = new Intent (this,UpcomingEventsActivity.class);
        startActivity(intent);
    }

    public void onCreatedEventsClick (View view){
        Intent intent  =new Intent(this,CreatedEventsActivity.class);
        startActivity(intent);
    }

}
