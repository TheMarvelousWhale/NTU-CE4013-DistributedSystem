package com.company;


public class Client {

    public static void main(String[] args) throws Exception {
        UDPClient UDP_Sender = new UDPClient("localhost", 9000);
        Utils utils = new TerminalUtils();
        ClientUserMgr clientUserMgr = ClientUserMgr.getInstance(UDP_Sender);
        ClientFacilityMgr facilityMgr = ClientFacilityMgr.getInstance(UDP_Sender);

        String loggedInUser = null;

        while (loggedInUser == null) {
            int option = utils.UserInputOptions(1, 2, "Welcome! Please select an option: \n1. Register" +
                    " \n2. Login", "Invalid option! \nPlease choose a valid option: ");
            switch (option) {
                case 1:
                    clientUserMgr.Register(utils);
                    break;
                case 2:
                    loggedInUser = clientUserMgr.Login(utils);
                    break;
                default:
                    break;
            }
        }

        String[] facilities = facilityMgr.getFacilities(utils);
        int num = 1;
        for (String facility: facilities){
            utils.println(num + ". " + facility);
            num++;
        }

    }
}
