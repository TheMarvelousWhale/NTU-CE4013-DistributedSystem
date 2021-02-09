package com.company;

import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws Exception {
        UDPServer Neki = new UDPServer();  //initiate UDP Server
        ServerUserMgr Viet = ServerUserMgr.getInstance();   //initiate UserMgr
        RequestHandler requestHandler = new RequestHandler();

        requestHandler.registerService("UserService" , Viet);

        String request = "";
        while (true) {
            request = Neki.receiveRequests();
            System.out.println(request);
            requestHandler.handleRequest(request, Neki);
        }
    }
}
