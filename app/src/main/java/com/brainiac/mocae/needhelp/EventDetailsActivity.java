package com.brainiac.mocae.needhelp;

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
    private TextView joinedPeopleTxtView;
    private Button joinButton;
    private String location;
    private HelpRequest mCurrentEvent;
    private String mCurrentEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        nameTxtView = (TextView) findViewById(R.id.nameTxtName);
        descriptionTxtView = (TextView) findViewById(R.id.descriptionTxtView);
        peopleTxtView = (TextView) findViewById(R.id.minRequiredPeopleTxtView);
        joinedPeopleTxtView = (TextView) findViewById(R.id.numberOfJoinedPeopleTxtView);
        startDateTxtView = (TextView) findViewById(R.id.startDateTxtView);
        endDateTxtView = (TextView) findViewById(R.id.endDateTxtView);
        tagTxtView = (TextView) findViewById(R.id.tagTxtView);
        locationTxtView = (TextView) findViewById(R.id.locationTxtView);
        joinButton = (Button) findViewById(R.id.joinEventBtn);

        String extra = getIntent().getStringExtra("HelpRequest");
        String[] parts = extra.split("\\|");
        String pos = parts[0];
        String type = parts[1];

        if (type.equals("JOINED")){
            mCurrentEvent = DataStorage.getInstance().getJoinedRequest(pos);
        } else if (type.equals("CREATED")) {
            mCurrentEvent = DataStorage.getInstance().getCreatedRequest(pos);
        } else {
            mCurrentEvent = DataStorage.getInstance().getRequest(pos);
        }

        if (mCurrentEvent.AdminID.equals(DataStorage.getInstance().GetCurrentUserId())){
            joinButton.setText("Cancel request");
        } else {
            boolean isJoined = DataStorage.getInstance().hasCurrentUserJoinedEvent(mCurrentEvent.ID);
            joinButton.setText(isJoined ? R.string.unjoin_button : R.string.join_button);
        }

        display(mCurrentEvent);
    }

    private void display (HelpRequest helpRequest) {
        nameTxtView.setText(helpRequest.Name);
        descriptionTxtView.setText(helpRequest.Description);
        peopleTxtView.setText("" + helpRequest.NumberOfPeople);
        joinedPeopleTxtView.setText("" + helpRequest.NumberOfJoinedPeople + "/" + helpRequest.NumberOfPeople);
        startDateTxtView.setText(helpRequest.StartDate);
        endDateTxtView.setText(helpRequest.EndDate);
        tagTxtView.setText(helpRequest.Tag);
        locationTxtView.setText(helpRequest.Location);
        location = helpRequest.Location;
        mCurrentEventID = helpRequest.ID;
    }

    public void onLocationClick (View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("NEWLOCATION", location);
        startActivity(intent);
    }

    public void onJoinClick (View view) {
        if (mCurrentEvent.AdminID.equals(DataStorage.getInstance().GetCurrentUserId())){
            DataStorage.getInstance().deleteRequest(mCurrentEvent);
            Intent intent = new Intent (this,MainActivity.class);
            startActivity(intent);
        } else {
            DataStorage.getInstance().saveJoinedEvent(mCurrentEvent.ID, new CallBack() {
                @Override
                public void onSuccess() {
                    boolean isJoined = DataStorage.getInstance().hasCurrentUserJoinedEvent(mCurrentEvent.ID);
                    joinButton.setText(isJoined ? R.string.unjoin_button : R.string.join_button);
                    mCurrentEvent = DataStorage.getInstance().getHelpRequest(mCurrentEventID);
                    display(mCurrentEvent);
                }
            });
        }
    }

}
