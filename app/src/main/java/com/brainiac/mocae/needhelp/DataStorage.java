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
    private DatabaseReference mDatabaseRefHelpRequests;
    private DatabaseReference mDatabaseRefJoinRequestsIds;
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
        helpRequestsList.clear();
        mDatabaseRefHelpRequests = FirebaseDatabase.getInstance().getReference().child("helpRequestsList");
        return mDatabaseRefHelpRequests;
    }

    private ArrayList<HelpRequest> helpRequestsList = new ArrayList<>();
    private ArrayList<HelpRequest> joinedHelpRequestsList = new ArrayList<>();

    private boolean gotJoinedEventList = false;
    private UserJoinedRequest mUserJoinedRequest = new UserJoinedRequest();

    public void Reset() {
        mCurrentUserId = "";
        helpRequestsList.clear();
        joinedHelpRequestsList.clear();
        gotJoinedEventList = false;
        mUserJoinedRequest = null;
    }

    public boolean hasCurrentUserJoinedEvent(String eventId) {
        if (mUserJoinedRequest == null) {
            return false;
        }

        for (String joinedEventId : mUserJoinedRequest.joinedRequestsIDs) {
            if (joinedEventId.equals(eventId)) {
                return true;
            }
        }

        return false;
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
        mDatabaseRefJoinRequestsIds.orderByChild("userID").equalTo(mCurrentUserId).addChildEventListener(childEventListener);
        mDatabaseRefJoinRequestsIds.addChildEventListener(childEventListener);

        return mUserJoinedRequest;
    }

    public Query getCreatedEvents(){
        return mDatabaseRefHelpRequests.orderByChild("AdminID").equalTo(mCurrentUserId);
    }

    public void getUserJoinedEvents(final List<String> joinedEventsId){
        mDatabaseRefHelpRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
                joinedHelpRequestsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    UserHelpRequest hr = ds.getValue(UserHelpRequest.class);
                    for (String id : joinedEventsId ){
                        if (id.equals(hr.ID)) {
                            joinedHelpRequestsList.add(hr);
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
        return joinedHelpRequestsList;
    }

    public DataStorage() {
        mDatabaseRefHelpRequests = FirebaseDatabase.getInstance().getReference().child("helpRequestsList");
        mDatabaseRefJoinRequestsIds = FirebaseDatabase.getInstance().getReference().child("joinedRequests");
    }

    public void saveRequest(HelpRequest request) {
        request.ID = UUID.randomUUID().toString();
        mDatabaseRefHelpRequests.child(request.ID).setValue(request);
    }

    public void saveJoinedEvent(final String helpRequestId, final CallBack callBack)
    {
        if (mUserJoinedRequest == null)
            mUserJoinedRequest = new UserJoinedRequest();
        if (mUserJoinedRequest.joinedRequestsIDs.contains(helpRequestId)) {
            mUserJoinedRequest.joinedRequestsIDs.remove(helpRequestId);

            for (int i = 0; i < joinedHelpRequestsList.size(); i ++) {
                HelpRequest hr = joinedHelpRequestsList.get(i);
                if (hr.ID.equals(helpRequestId)) {
                    joinedHelpRequestsList.remove(hr);
                    hr.NumberOfJoinedPeople -= 1;
                    mDatabaseRefHelpRequests.child(hr.ID).setValue(hr);
                    for (int j = 0; j< helpRequestsList.size(); j ++) {
                        HelpRequest hr2 = helpRequestsList.get(j);
                        if (hr2.ID == helpRequestId) {
                            helpRequestsList.remove(j);
                            helpRequestsList.add(hr);
                        }
                    }
                    break;
                }
            }
        } else {
            mUserJoinedRequest.joinedRequestsIDs.add(helpRequestId);
        }

        mDatabaseRefJoinRequestsIds.child(mCurrentUserId).setValue(mUserJoinedRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SuccessListener"," Success");
                if (mUserJoinedRequest.joinedRequestsIDs.contains(helpRequestId)){
                    for (int i = 0; i< helpRequestsList.size(); i ++){
                        HelpRequest hr = helpRequestsList.get(i);
                        if (hr.ID == helpRequestId){
                            hr.NumberOfJoinedPeople += 1;
                            joinedHelpRequestsList.add(hr);
                            mDatabaseRefHelpRequests.child(hr.ID).setValue(hr);
                            break;
                        }
                    }
                }
                callBack.onSuccess();
            }
        });

    }

    public void addRequest(HelpRequest request) {
        helpRequestsList.add(request);
    }

    public HelpRequest getHelpRequest (String id){
        for (int i = 0; i < helpRequestsList.size(); i ++)
            if (helpRequestsList.get(i).ID.equals(id))
                return helpRequestsList.get(i);
        return null;
    }

    public HelpRequest getJoinedRequest (String position){
        int pos = Integer.parseInt(position);
        if (pos < 0 || pos > joinedHelpRequestsList.size())
            return null;

        return joinedHelpRequestsList.get(pos);
    }

    public HelpRequest getRequest(String position) {
        int pos = Integer.parseInt(position);
        if (pos < 0 || pos > helpRequestsList.size())
            return null;

        return helpRequestsList.get(pos);
    }

    public HelpRequest getCreatedRequest (String id){
        for (int i = 0; i < helpRequestsList.size(); i ++){
            if (helpRequestsList.get(i).ID.equals(id))
                return helpRequestsList.get(i);
        }

        return null;
    }

    public void deleteRequest (HelpRequest helpRequest){
        HelpRequest hr = null;
        for (int i = 0; i < helpRequestsList.size(); i ++){
            hr = helpRequestsList.get(i);
            if (hr.ID.equals(helpRequest.ID)) {
                helpRequestsList.remove(i);
                mDatabaseRefHelpRequests.child(hr.ID).setValue(null);
                return;
            }
        }
    }
}
