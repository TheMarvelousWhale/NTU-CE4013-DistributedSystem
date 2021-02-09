package com.company;

import java.util.*;

public class ServerFacilityMgr implements ServiceMgr{

    private static ServerFacilityMgr single_instance = null;
    public HashMap<String, Facility> FacilityRecords;

    //I will make this a singleton class
    private ServerFacilityMgr(){
        this.FacilityRecords = new HashMap<>();

        //create facil
        Facility[] FacilityArray = new Facility[] {
                new Facility("cornhub",0),
                new Facility("swimming-pool",1),
                new Facility("library",2),
        };
        //register them in Mgr
        for (Facility f: FacilityArray)
            this.addObject(f.name,f);
    }

    public static ServerFacilityMgr getInstance() {
        if (single_instance == null)
            single_instance = new ServerFacilityMgr();
        return single_instance;
    }


    public String getFacilities(){
        String facilities = "";
        Iterator hmIterator = this.FacilityRecords.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            facilities += mapElement.getKey() + "/";
        }
        return facilities;
    }


    @Override
    public boolean addObject(String name, Object facility) {
        if (!this.FacilityRecords.containsKey(name)) {
            this.FacilityRecords.put(name, (Facility) facility);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean checkObject(String key) {
        return this.FacilityRecords.containsKey(key);
    }

    @Override
    public boolean updateObject(String key, String[] operation) {
        return false;
    }

    @Override
    public void handleRequest(String[] requestSequence, UDPServer sender) {
        switch (requestSequence[1]){
            case "getFacilities":     // getFacilities
                sender.sendMessage(this.getFacilities());
                break;

            case "checkObject":     // checkObject/&facility
                if (checkObject(requestSequence[2]))
                    sender.sendSuccessMessage();
                else
                    sender.sendFailureMessage();
                break;

            case "queryFacility":       //queryFacility/$facility
                sender.sendMessage(this.FacilityRecords.get(requestSequence[2]).queryAvailability());
                break;
//            case "updateObject":


            default:
                sender.sendMessage("MEOW");
                break;
        }
    }
}
