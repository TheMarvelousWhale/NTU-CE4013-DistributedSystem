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
        String[] facilities;
        facilities = facilityMgr.getFacilities(utils);
        facilities[facilities.length-1] = "Logout";
        int choice = 0;
        String selectedFacility;

        String[] facilityOptions = {"Main Menu:",
                "\t1. Query this facility",
                "\t2. Book this facility",
                "\t3. Change a booking",
                "\t4. Monitor this facility",
                "\t5. Show current booking",
                "\t6. Extend booking",
        };

        while(true){
            int num = 1;
            for (String facility: facilities){
                utils.println(num + ". " + facility);
                num++;
            }

            choice = utils.UserInputOptions(1, facilities.length, "Please select a facility: ",
                    "Invalid choice, please try again: ");

            if (choice >= facilities.length)
                break;

            selectedFacility = facilities[choice-1];

            for (String opt : facilityOptions) {  // print facility options
                utils.println(opt);
            }

            choice = utils.UserInputOptions(1, facilityOptions.length, "Please select an option: ",
                    "Invalid choice, please try again: ");

            String bookingID;

            switch (choice){
                case 1:
                    facilityMgr.queryFacility(utils, selectedFacility);
                    break;

                case 2:
                    facilityMgr.bookFacility(utils, loggedInUser, selectedFacility);
                    break;

                case 3:
                    bookingID = utils.getBookingID();
                    facilityMgr.changeBooking(utils, bookingID, selectedFacility);

                case 4:
                    facilityMgr.monitorFacility(selectedFacility, loggedInUser, utils);

                case 5:
                    facilityMgr.getUserBookings(selectedFacility, loggedInUser, utils);

                default:
                    break;
            }

        }


    }


}
