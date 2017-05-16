package com.brainiac.mocae.needhelp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferHelpActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_help);

        final ListView listview = (ListView) findViewById(android.R.id.list);

        DatabaseReference mDatabase = DataStorage.getInstance().getRefHelpRequests();
        ListAdapter firebaseAdapter = new FirebaseListAdapter<HelpRequest>(this, HelpRequest.class, R.layout.help_request_layout, mDatabase){
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
                Intent intent = new Intent(OfferHelpActivity.this,EventDetailsActivity.class);
                intent.putExtra("HelpRequest", position);
                ///Log.d("onClick","Offer");
                startActivity(intent);
            }

        });
    }
}
