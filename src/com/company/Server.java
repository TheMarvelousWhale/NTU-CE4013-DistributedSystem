package com.company;

import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws Exception {
        UDPServer Neki = new UDPServer();  //initiate UDP Server
        ServerUserMgr userMgr = ServerUserMgr.getInstance(Neki);   //initiate UserMgr
        RequestHandler requestHandler = new RequestHandler();
        ServerFacilityMgr facilityMgr = ServerFacilityMgr.getInstance(Neki);

        requestHandler.registerService("UserService" , userMgr);
        requestHandler.registerService("FacilityService", facilityMgr);

        String[] requestSequence;

        while (true) {
            requestSequence = Neki.receiveRequests();
            if (requestSequence == null){
                System.out.println("Signal was lost, resending message.");
                continue;
            }
            requestHandler.handleRequest(requestSequence);
        }
    }
}
