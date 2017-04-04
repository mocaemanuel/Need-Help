package com.brainiac.mocae.needhelp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mocae on 1/11/2017.
 */

public class HelpRequest{
    public String Name;
    public String Description;
    public Integer NumberOfPeople;
    public String StartDate;
    public String EndDate;
    public String Tag;
    public String Location;
    public String AdminID;
    public String ID;
}

class UserJoinedRequest extends HelpRequest {
    public String userID;
    public List<String> joinedRequestsIDs = new ArrayList<>();
}