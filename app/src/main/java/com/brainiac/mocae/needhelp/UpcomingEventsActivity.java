package com.brainiac.mocae.needhelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by mocae on 4/8/2017.
 */

public class UpcomingEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        final ListView listview = (ListView) findViewById(android.R.id.list);

        //final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, DataStorage.getInstance().getHelpRequests(true));
       // listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(UpcomingEventsActivity.this,EventDetailsActivity.class);
                intent.putExtra("HelpRequest", position);
                startActivity(intent);
            }

        });
    }
}
