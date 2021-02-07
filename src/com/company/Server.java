package com.company;

import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws Exception {
        UDPServer Neki = new UDPServer();  //initiate UDP Server
        ServerUserMgr Viet = ServerUserMgr.getInstance();   //initiate UserMgr

        String request = "";
        while (true) {
            request = Neki.receiveRequests();
            String[] reqSequence = request.split("/", -1);

            switch (reqSequence[0]){
                case "Register":
                    if (reqSequence[1].equals("AddNewUser")){
                        Viet.addNewuser(reqSequence[2], reqSequence[3], Neki);
                    }
                    else if (reqSequence[1].equals("CheckUsernameExists")){
                        Viet.checkUsername(reqSequence[1], Neki);
                    }
                    break;

                case "Login":
                    Viet.login(reqSequence[1], reqSequence[2], Neki);
                    break;
            }
        }
    }
}
