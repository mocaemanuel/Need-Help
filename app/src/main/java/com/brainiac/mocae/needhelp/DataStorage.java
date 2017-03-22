package com.brainiac.mocae.needhelp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveRequest(HelpRequest request) {
        mDatabase.child("helpRequest").setValue(request);
        helpRequests.add(request);
    }

}
