package com.liclog;

import java.util.HashMap;
import java.util.ArrayList;

public class LicFeature
{
    public String featureName;
    public Integer nLicsTotal;
    public Integer nLicsIssued;

    public ArrayList<HashMap<String, String>> usersList;

    public boolean error;
    public String errorMsg;

    public LicFeature()
    {
        usersList = new ArrayList<HashMap<String, String>>();
        error = false;
        errorMsg = "";
    }

    public LicFeature(String featureName, boolean error, String errorMsg)
    {
        usersList = new ArrayList<HashMap<String, String>>();
        this.featureName = featureName;
        this.error = error;
        this. errorMsg = errorMsg;
    }
}
