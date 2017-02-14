package com.brainiac.mocae.needhelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView nameTxtView;
    private TextView descriptionTxtView;
    private TextView peopleTxtView;
    private TextView startDateTxtView;
    private TextView endDateTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        nameTxtView = (TextView) findViewById(R.id.nameTxtName);
        descriptionTxtView = (TextView) findViewById(R.id.descriptionTxtView);
        peopleTxtView = (TextView) findViewById(R.id.minRequiredPeopleTxtView);
        startDateTxtView = (TextView) findViewById(R.id.startDateTxtView);
        endDateTxtView = (TextView) findViewById(R.id.endDateTxtView);

        int pos = getIntent().getIntExtra("HelpRequest", -1);
        display(DataStorage.getInstance().getHelpRequests().get(pos));
    }

    private void display (HelpRequest helpRequest) {
        nameTxtView.setText(helpRequest.Name);
        descriptionTxtView.setText(helpRequest.Description);
        peopleTxtView.setText("" + helpRequest.NumberOfPeople);
        startDateTxtView.setText(helpRequest.StartDate);
        endDateTxtView.setText(helpRequest.EndDate);
    }
}
