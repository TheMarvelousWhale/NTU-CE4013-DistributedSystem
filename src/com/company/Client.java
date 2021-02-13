package com.company;


public class Client {


    public static void main(String[] args) throws Exception {
        UDPClient UDP_Sender = new UDPClient("localhost", 9000);
        Utils utils = new TerminalUtils();
        ClientUserMgr clientUserMgr = ClientUserMgr.getInstance(UDP_Sender);
        ClientFacilityMgr facilityMgr = ClientFacilityMgr.getInstance(UDP_Sender);

        String loggedInUser = null;

        int mode = utils.UserInputOptions(1, 3, "Select a mode: \n1. Send packet normally \n" +
                "2. Packet loss at client side \n3. Packet loss at server side",
                "Invalid option! \nPlease choose a valid option: ");

        switch (mode){
            case 1:
                UDP_Sender.setMode("normal");
                break;
            case 2:
                UDP_Sender.setMode("client signal loss");
                break;
            case 3:
                UDP_Sender.setMode("server signal loss");   // havent figure this out yet
                break;
            default:
                break;

        }

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
            int extensionTime = 0;
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
                    break;
                case 4:
                    facilityMgr.monitorFacility(selectedFacility, loggedInUser, utils);
                    break;
                case 5:     // Non-idempotent function
                    facilityMgr.getUserBookings(selectedFacility, loggedInUser, utils);
                    break;
                case 6:     // Idempotent function
                    //LAST FUNCTION TO IMPLEMENT EXTEND BOOKING
                    bookingID = utils.getBookingID();
                    utils.println("How many slots do you want to extend your booking by (only up to 3hrs)?");
                    extensionTime = utils.checkUserIntInput(1,3);
                    facilityMgr.extendBooking(selectedFacility, bookingID, extensionTime, utils);
                    break;

                default:
                    break;
            }

        }


    }


}
