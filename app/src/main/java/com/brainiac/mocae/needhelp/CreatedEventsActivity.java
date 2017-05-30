package com.brainiac.mocae.needhelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CreatedEventsActivity extends AppCompatActivity {
    private MySimpleArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_events);


        final ListView listview = (ListView) findViewById(android.R.id.list);

        final Query database = DataStorage.getInstance().getCreatedEvents();

        ListAdapter firebaseAdapter = new FirebaseListAdapter<HelpRequest>(this, HelpRequest.class, R.layout.help_request_layout, database){
            @Override
            protected void populateView(View view, HelpRequest message, int x){
                TextView nameTxtView = (TextView) view.findViewById(R.id.nameTxtView);
                TextView peopleTxtView = (TextView) view.findViewById(R.id.peopleTxtView);
                TextView dateTxtView = (TextView) view.findViewById(R.id.dateTxtView);

                nameTxtView.setText(message.Name);
                peopleTxtView.setText(message.NumberOfPeople + "" + " persons requered");
                dateTxtView.setText(message.StartDate + " -- " + message.EndDate);

                DataStorage.getInstance().addRequest(message);
            }
        };
        listview.setAdapter(firebaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(CreatedEventsActivity.this,EventDetailsActivity.class);
                String ID = DataStorage.getInstance().getRequest(position + "").ID;
                intent.putExtra("HelpRequest", ID + "|CREATED");
                startActivity(intent);
            }

        });
    }
}
