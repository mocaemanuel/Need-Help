package com.brainiac.mocae.needhelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CreatedEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_events);

        final ListView listview = (ListView) findViewById(android.R.id.list);

        final ArrayList<HelpRequest> database = DataStorage.getInstance().getJoinedEvents();
        MySimpleArrayAdapter arrayAdapter = new MySimpleArrayAdapter(this,database);

        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(CreatedEventsActivity.this,EventDetailsActivity.class);
                intent.putExtra("HelpRequest", position);
                ///Log.d("onClick","Ask");
                startActivity(intent);
            }

        });
    }
}
