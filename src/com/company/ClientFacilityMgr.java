package com.company;

import java.io.IOException;

public class ClientFacilityMgr {
    private static ClientFacilityMgr single_instance = null;
    private static final String SERVICENAME = "FacilityService/";

    private ClientFacilityMgr(){

    }


    public static ClientFacilityMgr getInstance() {
        if (single_instance == null)
            single_instance = new ClientFacilityMgr();
        return single_instance;
    }

    public String[] getFacilities(Utils utils, UDPClient sender){
        String message = SERVICENAME;
        message += "getFacilities";
        String response = "";
        try {
            response = sender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.split("/", -1);
    }
}
