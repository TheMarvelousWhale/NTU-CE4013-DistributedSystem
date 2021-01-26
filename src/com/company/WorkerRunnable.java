package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * This is the what the server will execute for each thread it spawns.
 */
public class WorkerRunnable implements Runnable {
    private static final long POLLING_DURATION = 10;  //seconds

    protected Socket clientSocket = null;
    public static int numThreads = 0;
    public long thisThreadLastModified;
    private Utils u;


    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
        numThreads++;
        this.u = null;
        try {
            this.u = new SocketUtils(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int id = numThreads;
        System.out.println("[Thread "+id+ "]: ready to serve");

        FacilityMgr Neki = FacilityMgr.getInstance();

        UserMgr Viet = UserMgr.getInstance();  //Added this new user manager



        Utils utils = this.u;
        /**
         * User will now log in instead of typing their preferred name
         *
         * And the user object will be the one calling the facility methods
         * User books -> Facility
         * User queries -> Facility     etc....
         */
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

        String welcome_msg = String.format("Welcome User %s to MOLIBMA 2.0", loggedInUser.username);

        while (true) {

            utils.println(welcome_msg);                         //piggy back the input's ACK

            //choose facility
            Facility f = Neki.getUserToChooseFacil(utils);      //quit client program happen here
            if (f == null)
                break;

            //print the menu
            printFacilMenu(utils);                             //piggy back the input's ACK

            //get the data they want
            int choice = utils.UserInputOptions(1, 6, "Please choose an option: ",
                    "Invalid options!\nPlease choose a valid option: ");                    //thank you for the carry

            long time = System.currentTimeMillis();
            System.out.println("[Thread " + id + "]: Received this from client: " + choice +" at time "+time);

            switch (choice) {
                case 1:
                    loggedInUser.queryFacilityAvailability(utils, f);
                    break;
                case 2:
                    loggedInUser.bookFacility(utils, f);
                    thisThreadLastModified = System.currentTimeMillis();
                    break;
                case 3:
                    String bookingID = utils.getBookingID();
                    f.changeBooking(bookingID,utils);
                    thisThreadLastModified = System.currentTimeMillis();
                    break;
                case 4:
                    f.register(this);
                    int dur = utils.UserInputOptions(1, 1000,
                            "Enter the duration (in hours) to monitor from (1-1000) inclusive",
                            "Please reenter the duration (1-1000) inclusive: ");
                    utils.println("Start monitoring: ");
                    try {
                        Thread.sleep(dur * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }   finally {
                        utils.println("Stopping monitor");
                        f.deregister(this);
                    }
                    break;
                case 5:
                    loggedInUser.getBookingRecords(utils, f);
                    break;

            }


        }
        utils.println(RRA.SESSION_TERMINATE);
        utils.println(RRA.ACK);                     //
        //clean up this thread
        System.out.println("[Thread "+id+"]: Finished it job.");

    }
    public void monitorFacility(Facility f) {
        Utils utils = this.u;
        long l = f.lastModified;
        String noti = String.format("=====  %s AT %02d:%02d  =====",f.name.toUpperCase(), (l/36000/1000)%24,(l/1000/60)%60);
        utils.println(noti);
        f.queryAvailability(utils);

    }

    private void printFacilMenu(Utils utils) {
        String[] opts = {"Main Menu:",
                "\t1. Query this facility",
                "\t2. Book this facility",
                "\t3. Change a booking",
                "\t4. Monitor this facility",
                "\t5. Show current booking",
                "\t6. TBC 2"
        };
        for (String opt : opts) {
            utils.println(opt);
        }
    }

}
