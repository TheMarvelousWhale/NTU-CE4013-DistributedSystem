package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerFacilityMgr implements ServiceMgr {

    private static ServerFacilityMgr single_instance = null;
    public HashMap<String, Facility> FacilityRecords;
    UDPServer sender;

    //I will make this a singleton class
    private ServerFacilityMgr(UDPServer sender){
        this.FacilityRecords = new HashMap<>();
        this.sender = sender;
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

    public static ServerFacilityMgr getInstance(UDPServer sender) {
        if (single_instance == null)
            single_instance = new ServerFacilityMgr(sender);
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
    public String updateObject(String key, String[] requestSequence) {
        String response = "fail";
        switch (requestSequence[3]){
            case "book":    // updateObject/$facility/book/  username/date_input/startTime/endTime
                response = this.FacilityRecords.get(key).book(requestSequence[4], requestSequence[5], requestSequence[6],
                        requestSequence[7], this.sender);
                break;

            case "changeBooking":    // updateObject/facility/changeBooking/bookingID/offset
                response = this.FacilityRecords.get(key).changeBooking(requestSequence[4], requestSequence[5], this.sender);
                break;
        }

        return response;
    }

    @Override
    public void handleRequest(String[] requestSequence) {
        switch (requestSequence[1]){
            case "getFacilities":     // getFacilities
                sender.sendMessage(this.getFacilities());
                break;

            case "checkObject":     // checkObject/$facility
                if (checkObject(requestSequence[2]))
                    sender.sendSuccessMessage();
                else
                    sender.sendFailureMessage();
                break;

            case "queryFacility":       //queryFacility/$facility
                sender.sendMessage(this.FacilityRecords.get(requestSequence[2]).queryAvailability());
                break;

            case "updateObject":        // for booking:  updateObject/$facility/book/username/date_input/startTime/endTime
                sender.sendMessage(updateObject(requestSequence[2], requestSequence));
                break;

            case "monitor":     //monitor/register/facility/username/Port
                if (requestSequence[2].equals("register"))
                    this.FacilityRecords.get(requestSequence[3]).registerUser(requestSequence[4], sender.getReturnAddress(),
                        requestSequence[5]);
                else        //monitor/unregister/facility/username
                    this.FacilityRecords.get(requestSequence[3]).unregisterUser(requestSequence[4]);
                sender.sendSuccessMessage();
                break;

            case "getBookings": //getBookings/facility/username
                sender.sendMessage(this.FacilityRecords.get(requestSequence[2]).showRecords(requestSequence[3]));
                break;

            case "extendBooking":  //extendBooking/facility/bookingID/extenstionTime
                sender.sendMessage(this.FacilityRecords.get(requestSequence[2]).extendBooking(requestSequence[3],
                        requestSequence[4]));
                break;

            default:
                sender.sendMessage("meow");
                break;
        }
    }
}
