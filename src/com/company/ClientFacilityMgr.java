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
}
