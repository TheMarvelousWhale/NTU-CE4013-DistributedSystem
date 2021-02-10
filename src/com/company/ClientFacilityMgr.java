package com.company;

import java.io.IOException;

public class ClientFacilityMgr {
    private static ClientFacilityMgr single_instance = null;
    private static final String SERVICENAME = "FacilityService/";
    UDPClient sender;

    private ClientFacilityMgr(UDPClient sender){
        this.sender = sender;
    }


    public static ClientFacilityMgr getInstance(UDPClient sender) {
        if (single_instance == null)
            single_instance = new ClientFacilityMgr(sender);
        return single_instance;
    }

    public String[] getFacilities(Utils utils){
        String message = SERVICENAME;
        message += "getFacilities";
        String response = "";
        try {
            response = this.sender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.split("/", -1);
    }

    public void queryFacility(Utils utils, String facility){
        String message = SERVICENAME;
        message += "queryFacility/"+facility;
        String response = "";
        try {
            response = this.sender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        utils.println(response);
    }

    public void bookFacility(Utils utils, String username, String facility){
        String message = SERVICENAME;
        message += "updateObject/";
        message += facility + "/book/" + username + "/";
        //updateObject/$facility/book/username/date_input/startTime/endTime

        utils.println("For the day (1: Monday, 2: Tuesday, 3: Wednesday, 4: Thursday, 5: Friday, 6: Saturday, 7: Sunday): ");
        int date_input = utils.checkUserIntInput(1,7);
        message += date_input + "/";

        utils.println("For the start time: ");
        int startTime = utils.checkUserIntInput(0,23);
        message += startTime + "/";

        utils.println("For the end time: ");
        int endTime = utils.checkUserIntInput(0,23);
        message += endTime;


        String response = "";

        try {
            response = this.sender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        utils.println(response);
    }
}
