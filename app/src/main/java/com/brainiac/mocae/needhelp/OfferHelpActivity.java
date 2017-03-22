package com.brainiac.mocae.needhelp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferHelpActivity extends ListActivity {

        private DatabaseReference mDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_offer_help);

            mDatabase = FirebaseDatabase.getInstance().getReference();

            final ListView listview = (ListView) findViewById(android.R.id.list);

            final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, DataStorage.getInstance().getHelpRequests());
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Intent intent = new Intent(OfferHelpActivity.this,EventDetailsActivity.class);
                    intent.putExtra("HelpRequest", position);
                    startActivity(intent);
                }

            });
        }
}
