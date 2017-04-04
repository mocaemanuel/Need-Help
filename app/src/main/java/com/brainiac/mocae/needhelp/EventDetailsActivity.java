package com.brainiac.mocae.needhelp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView nameTxtView;
    private TextView descriptionTxtView;
    private TextView peopleTxtView;
    private TextView startDateTxtView;
    private TextView endDateTxtView;
    private TextView tagTxtView;
    private TextView locationTxtView;
    private Button joinButton;
    private String location;
    private HelpRequest mCurrentEvent;
    private UserJoinedRequest mCurrentUserJoinedEvents;

    private String mCurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        nameTxtView = (TextView) findViewById(R.id.nameTxtName);
        descriptionTxtView = (TextView) findViewById(R.id.descriptionTxtView);
        peopleTxtView = (TextView) findViewById(R.id.minRequiredPeopleTxtView);
        startDateTxtView = (TextView) findViewById(R.id.startDateTxtView);
        endDateTxtView = (TextView) findViewById(R.id.endDateTxtView);
        tagTxtView = (TextView) findViewById(R.id.tagTxtView);
        locationTxtView = (TextView) findViewById(R.id.locationTxtView);
        joinButton = (Button) findViewById(R.id.joinEventBtn);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mCurrentUserId = user.getUid();

        int pos = getIntent().getIntExtra("HelpRequest", -1);
        mCurrentEvent = DataStorage.getInstance().getHelpRequests(false).get(pos);

        boolean isJoined = DataStorage.getInstance().isCurrentUserJoinedOnEvent(mCurrentUserId, mCurrentEvent.ID);
        joinButton.setText(isJoined ? R.string.unjoin_button : R.string.join_button);

        display(mCurrentEvent);
    }

    private void display (HelpRequest helpRequest) {
        nameTxtView.setText(helpRequest.Name);
        descriptionTxtView.setText(helpRequest.Description);
        peopleTxtView.setText("" + helpRequest.NumberOfPeople);
        startDateTxtView.setText(helpRequest.StartDate);
        endDateTxtView.setText(helpRequest.EndDate);
        tagTxtView.setText(helpRequest.Tag);
        locationTxtView.setText(helpRequest.Location);
        location = helpRequest.Location;
    }

    public void onLocationClick (View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("NEWLOCATION", location);
        startActivity(intent);
    }

    public void onJoinClick (View view) {
        if (mCurrentUserJoinedEvents == null) {
            mCurrentUserJoinedEvents = new UserJoinedRequest();
            mCurrentUserJoinedEvents.userID = mCurrentUserId;
        }
        mCurrentUserJoinedEvents.joinedRequestsIDs.add(mCurrentEvent.ID);
    }

}
