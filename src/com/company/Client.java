package com.company;


public class Client {

    public static void main(String[] args) throws Exception {
        UDPClient UDP_Sender = new UDPClient("localhost", 9000);
        FacilityMgr Neki = FacilityMgr.getInstance();
        Utils utils = new TerminalUtils();
        ClientUserMgr Viet = ClientUserMgr.getInstance();
        String loggedInUser = null;

        while (loggedInUser == null) {
            int option = utils.UserInputOptions(1, 2, "Welcome! Please select an option: \n1. Register" +
                    " \n2. Login", "Invalid option! \nPlease choose a valid option: ");
            switch (option) {
                case 1:
                    Viet.Register(utils, UDP_Sender);
                    break;
                case 2:
                    loggedInUser = Viet.Login(utils, UDP_Sender);
                    break;
                default:
                    break;
            }
        }


    }
}
