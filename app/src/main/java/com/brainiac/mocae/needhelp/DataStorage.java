package com.brainiac.mocae.needhelp;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mocae on 1/17/2017.
 */

public class DataStorage {
    private static DataStorage instance = null;
    private DatabaseReference mDatabase;

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }

        return instance;
    }

    private ArrayList<HelpRequest> helpRequests = new ArrayList<>();
    public ArrayList<HelpRequest> getHelpRequests() {
        return helpRequests;
    }

    public DataStorage() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("helpRequests");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("NeedHelp", "childAddedToDatabase" + dataSnapshot.getKey() );

                for (DataSnapshot hrSnapshot: dataSnapshot.getChildren()){
                    HelpRequest hr = hrSnapshot.getValue(HelpRequest.class);
                    Log.d ("NeedHelp", "items " + hr.toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addChildEventListener(childEventListener);
    }

    public void saveRequest(HelpRequest request) {
        mDatabase.child(UUID.randomUUID().toString()).setValue(request);
        helpRequests.add(request);
    }
}
