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
    public ArrayList<HelpRequest> getHelpRequests(boolean force) {
        if (force == false)
            return helpRequests;

        helpRequests.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("helpRequests");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("NeedHelp", "childAddedToDatabase" + dataSnapshot.getKey());
                helpRequests.add(dataSnapshot.getValue(HelpRequest.class));

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
        try {
            childEventListener.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return helpRequests;
    }

    private boolean gotJoinedEventList = false;
    private UserJoinedRequest mUserJoinedRequest = new UserJoinedRequest();

    public boolean isCurrentUserJoinedOnEvent(String currentUserId, String eventId) {
        DataStorage.getInstance().getJoinedEvents(currentUserId);

        boolean isJoined = false;

        if (mUserJoinedRequest == null) {
            return isJoined;
        }

        for (String joinedEventId : mUserJoinedRequest.joinedRequestsIDs) {
            if (joinedEventId.equals(eventId)) {
                isJoined = true;
                break;
            }
        }

        return isJoined;
    }

    public UserJoinedRequest getJoinedEvents(String currentUserID){
        if (gotJoinedEventList)
            return mUserJoinedRequest;

        gotJoinedEventList = true;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("joinedRequests");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("NeedHelp", "joinedRequests");
                mUserJoinedRequest = dataSnapshot.getValue(UserJoinedRequest.class);
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
        mDatabase.orderByChild("userID").equalTo(currentUserID).addChildEventListener(childEventListener);
        mDatabase.addChildEventListener(childEventListener);

        return mUserJoinedRequest;
    }

    public DataStorage() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("helpRequests");
    }

    public void saveRequest(HelpRequest request) {
        request.ID = UUID.randomUUID().toString();
        mDatabase.child(request.ID).setValue(request);
    }
}
