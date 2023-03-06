package com.liclog;

import java.util.ArrayList;

public class LicGroup
{
    public String groupName;

    public ArrayList<String> machinesList;

    public LicGroup()
    {
        machinesList = new ArrayList<String>();
    }

    public LicGroup(String groupName)
    {
        machinesList = new ArrayList<String>();
        this.groupName = groupName;
    }
}
