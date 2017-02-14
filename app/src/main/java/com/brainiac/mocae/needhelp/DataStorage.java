package com.brainiac.mocae.needhelp;

import java.util.ArrayList;

/**
 * Created by mocae on 1/17/2017.
 */

public class DataStorage {
    private static DataStorage instance = null;
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

    public void saveRequest(HelpRequest request) {
        helpRequests.add(request);
    }
}
