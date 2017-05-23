package com.brainiac.mocae.needhelp;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mocae on 1/17/2017.
 */

public class DataStorage {
    private static DataStorage instance = null;
    private DatabaseReference mDatabaseHelpRequests;
    private DatabaseReference mDatabaseJoinRequestsIds;
    private String mCurrentUserId;

    public void SetCurrentUser(String currentUserId) {
        mCurrentUserId = currentUserId;
    }

    public String GetCurrentUserId() {
        return mCurrentUserId;
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public DatabaseReference getRefHelpRequests(){
        helpRequests.clear();
        mDatabaseHelpRequests = FirebaseDatabase.getInstance().getReference().child("helpRequests");
        return mDatabaseHelpRequests;
    }

    private ArrayList<HelpRequest> helpRequests = new ArrayList<>();

    private ArrayList<HelpRequest> joinedHelpRequests = new ArrayList<>();

    private ArrayList<HelpRequest> createdEvents = new ArrayList<>();

    private boolean gotJoinedEventList = false;
    private UserJoinedRequest mUserJoinedRequest = new UserJoinedRequest();

    public boolean isCurrentUserJoinedOnEvent(String currentUserId, String eventId) {
        DataStorage.getInstance().getJoinedEventsIds();

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

    public UserJoinedRequest getJoinedEventsIds(){
        if (gotJoinedEventList)
            return mUserJoinedRequest;

        gotJoinedEventList = true;
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("NeedHelp", "joinedRequests" + s);
                mUserJoinedRequest = dataSnapshot.getValue(UserJoinedRequest.class);
                getUserJoinedEvents(mUserJoinedRequest.joinedRequestsIDs);
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
        mDatabaseJoinRequestsIds.orderByChild("userID").equalTo(mCurrentUserId).addChildEventListener(childEventListener);
        mDatabaseJoinRequestsIds.addChildEventListener(childEventListener);

        return mUserJoinedRequest;
    }

    public Query getCreatedEvents(){
        return mDatabaseHelpRequests.orderByChild("AdminID").equalTo(mCurrentUserId);
    }

    public void getUserJoinedEvents(final List<String> joinedEventsId){
        mDatabaseHelpRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
                joinedHelpRequests.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    UserHelpRequest hr = ds.getValue(UserHelpRequest.class);
                    for (String id : joinedEventsId ){
                        if (id.equals(hr.ID)) {
                            joinedHelpRequests.add(hr);
                            Log.d("idd", " joined " + hr.ID);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<HelpRequest> getJoinedEvents (){
        return joinedHelpRequests;
    }

    public DataStorage() {
        mDatabaseHelpRequests = FirebaseDatabase.getInstance().getReference().child("helpRequests");
        mDatabaseJoinRequestsIds = FirebaseDatabase.getInstance().getReference().child("joinedRequests");
    }

    public void saveRequest(HelpRequest request) {
        request.ID = UUID.randomUUID().toString();
        mDatabaseHelpRequests.child(request.ID).setValue(request);
    }

    public void saveJoinedEvent(final String helpRequestId, final CallBack callBack)
    {
        if (mUserJoinedRequest.joinedRequestsIDs.contains(helpRequestId)) {
            mUserJoinedRequest.joinedRequestsIDs.remove(helpRequestId);

            for (int i = 0; i < joinedHelpRequests.size(); i ++) {
                HelpRequest hr = joinedHelpRequests.get(i);
                if (hr.ID.equals(helpRequestId)) {
                    hr.NumberOfJoinedPeople -= 1;
                    joinedHelpRequests.remove(hr);
                    mDatabaseHelpRequests.child(hr.ID).setValue(hr);
                    for (int j = 0; j< helpRequests.size(); j ++) {
                        HelpRequest hr2 = helpRequests.get(j);
                        if (hr2.ID == helpRequestId) {
                            helpRequests.remove(j);
                            helpRequests.add(hr);
                        }
                    }
                    break;
                }
            }
        } else {
            mUserJoinedRequest.joinedRequestsIDs.add(helpRequestId);
        }
        mDatabaseJoinRequestsIds.child(mCurrentUserId).setValue(mUserJoinedRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SuccessListener"," Success");
                if (mUserJoinedRequest.joinedRequestsIDs.contains(helpRequestId)){
                    for (int i = 0; i< helpRequests.size(); i ++){
                        HelpRequest hr = helpRequests.get(i);
                        if (hr.ID == helpRequestId){
                            joinedHelpRequests.add(hr);
                            hr.NumberOfJoinedPeople += 1;
                            mDatabaseHelpRequests.child(hr.ID).setValue(hr);
                            break;
                        }
                    }
                }
                callBack.onSuccess();
            }
        });

    }

    public void addRequest(HelpRequest request) {
        helpRequests.add(request);
    }

    public HelpRequest getHelpRequest (String id){
        for (int i = 0; i < helpRequests.size(); i ++)
            if (helpRequests.get(i).ID.equals(id))
                return helpRequests.get(i);
        return null;
    }

    public HelpRequest getRequest(int position) {
        if (position < 0 || position > helpRequests.size())
            return null;

        return helpRequests.get(position);
    }
}
