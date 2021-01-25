package com.company;

import java.util.UUID;


public class Main {

    public static void main(String[] args) {
	// write your code here
        Utils utils = new TerminalUtils();
        FacilityMgr Neki = FacilityMgr.getInstance();
        UserMgr Viet = UserMgr.getInstance();
        User loggedInUser = null;

        while (loggedInUser == null) {
            int option = utils.UserInputOptions(1, 2, "Welcome! Please select an option: \n1. Register" +
                    " \n2. Login", "Invalid option! \nPlease choose a valid option: ");
            switch (option) {
                case 1:
                    Viet.Register(utils);
                    break;
                case 2:
                    loggedInUser = Viet.Login(utils);
                    break;
                default:
                    break;
            }
        }

        while (true) {
            boolean keep_alive = true;
            String welcome_msg = String.format("Welcome User %s to MOLIBMA 2.0", loggedInUser.username).toString();
            System.out.println(welcome_msg);
            while (keep_alive) {
                //choose the facility

                Facility f = Neki.getUserToChooseFacil(utils);

                //print the facil menu
                Main.printFacilMenu();
                //Choosing the options for a facil
                int chosen_option = utils.UserInputOptions(1, 6, "Please choose an option: ",
                        "Invalid options!\nPlease choose a valid option: ");

                switch (chosen_option)
                {
                    case 1:
                        loggedInUser.queryFacilityAvailability(utils, f);
                        break;
                    case 2:
                        loggedInUser.bookFacility(utils, f);
                        break;
                    case 3:
                        loggedInUser.getBookingRecords(utils, f);
                        String bookingID = utils.getBookingID();
                        f.changeBooking(bookingID, utils);
                        break;
                    case 5:
                        loggedInUser.cancelBooking(utils, f);
                        break;
                    case 6:
                        f.showRecords(loggedInUser.username, utils);
                        String extendID = utils.getBookingID();
                        f.extendBooking(extendID, utils);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    public static void printFacilMenu(){
        String[] opts = {"Main Menu:",
                "\t1. Query a facility",
                "\t2. Book a facility",
                "\t3. Change a booking",
                "\t4. Monitor a facility",
                "\t5. Cancel Booking",
                "\t6. Extend Booking"
        };
        //print the menu
        for (String opt : opts) {
            System.out.println(opt);
        }
    }
}
