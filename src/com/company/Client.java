package com.company;


public class Client {

    public static void main(String[] args) throws Exception {
        UDPClient UDP_Sender = new UDPClient("localhost", 9000);
        Utils utils = new TerminalUtils();
        ClientUserMgr clientUserMgr = ClientUserMgr.getInstance();
        ClientFacilityMgr facilityMgr = ClientFacilityMgr.getInstance();

        String loggedInUser = null;

        while (loggedInUser == null) {
            int option = utils.UserInputOptions(1, 2, "Welcome! Please select an option: \n1. Register" +
                    " \n2. Login", "Invalid option! \nPlease choose a valid option: ");
            switch (option) {
                case 1:
                    clientUserMgr.Register(utils, UDP_Sender);
                    break;
                case 2:
                    loggedInUser = clientUserMgr.Login(utils, UDP_Sender);
                    break;
                default:
                    break;
            }
        }

        String[] facilities = facilityMgr.getFacilities(utils, UDP_Sender);
        int num = 1;
        for (String facility: facilities){
            utils.println(num + ". " + facility);
            num++;
        }

    }
}
