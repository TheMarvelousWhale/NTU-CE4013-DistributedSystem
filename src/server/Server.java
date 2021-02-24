package server;

public class Server {

    public static void main(String[] args) throws Exception {
        UDPServer Neki = new UDPServer();  //initiate UDP Server
        ServerUserMgr userMgr = ServerUserMgr.getInstance(Neki);   //initiate UserMgr
        ServerFacilityMgr facilityMgr = ServerFacilityMgr.getInstance(Neki);
        RequestHandler requestHandler = new RequestHandler();

        requestHandler.registerService("UserService" , userMgr);
        requestHandler.registerService("FacilityService", facilityMgr);

        String[] requestSequence;

        while (true) {
            requestSequence = Neki.receiveRequests();
            if (requestSequence == null){
                continue;
            }
            requestHandler.handleRequest(requestSequence);
        }
    }
}
