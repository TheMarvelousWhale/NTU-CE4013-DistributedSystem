package com.company;

import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws Exception {
        UDPServer Neki = new UDPServer();  //initiate UDP Server
        ServerUserMgr userMgr = ServerUserMgr.getInstance();   //initiate UserMgr
        RequestHandler requestHandler = new RequestHandler();
        ServerFacilityMgr facilityMgr = ServerFacilityMgr.getInstance();

        requestHandler.registerService("UserService" , userMgr);
        requestHandler.registerService("FacilityService", facilityMgr);

        String request = "";
        while (true) {
            request = Neki.receiveRequests();
            System.out.println(request);
            requestHandler.handleRequest(request, Neki);
        }
    }
}
