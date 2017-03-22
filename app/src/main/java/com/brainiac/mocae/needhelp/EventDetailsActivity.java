package com.brainiac.mocae.needhelp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView nameTxtView;
    private TextView descriptionTxtView;
    private TextView peopleTxtView;
    private TextView startDateTxtView;
    private TextView endDateTxtView;
    private TextView tagTxtView;
    private TextView locationTxtView;
    private String location;

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

        int pos = getIntent().getIntExtra("HelpRequest", -1);
        display(DataStorage.getInstance().getHelpRequests().get(pos));
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
        //setResult(Activity.RESULT_OK,intent);
        startActivity(intent);
    }
}
