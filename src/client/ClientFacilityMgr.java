package client;

import java.io.IOException;

public class ClientFacilityMgr {
    private static ClientFacilityMgr single_instance = null;
    private static final String SERVICENAME = "FacilityService/";
    UDPClient comms;  // communication device that uses UDP

    private ClientFacilityMgr(UDPClient comms){
        this.comms = comms;
    }


    public static ClientFacilityMgr getInstance(UDPClient comms) {
        if (single_instance == null)
            single_instance = new ClientFacilityMgr(comms);
        return single_instance;
    }

    public String[] getFacilities(Utils utils){
        String message = SERVICENAME;
        message += "getFacilities";
        String response = "";
        response = this.comms.sendMessage(message);
        return response.split("/", -1);
    }

    public void queryFacility(Utils utils, String facility){
        String message = SERVICENAME;
        message += "queryFacility/"+facility;
        String response = "";
        response = this.comms.sendMessage(message);
        utils.println(response);
    }

    public void bookFacility(Utils utils, String username, String facility){
        String message = SERVICENAME;
        message += "updateObject/";
        message += facility + "/book/" + username + "/";
        //updateObject/$facility/book/username/date_input/startTime/endTime

        utils.println("For the day (1: Monday, 2: Tuesday, 3: Wednesday, 4: Thursday, 5: Friday, 6: Saturday, 7: Sunday): ");
        int date_input = utils.checkUserIntInput(1,7);
        message += date_input + "/";

        utils.println("For the start time. \nEnter the start hour:");
        int startTime = utils.checkUserIntInput(0,23);
        startTime *= 4;
        utils.println("Enter the start minute ( 0 -> 0 min, 1 -> 15 min, 2 -> 30 min, 3 -> 45 min");
        startTime += utils.checkUserIntInput(0, 3);
        message += startTime + "/";

        utils.println("For the end time: \nEnter the end hour:");
        int endTime = utils.checkUserIntInput(0,23);
        endTime *= 4;
        utils.println("Enter the end minute ( 0 -> 0 min, 1 -> 15 min, 2 -> 30 min, 3 -> 45 min");
        endTime += utils.checkUserIntInput(0, 3);
        message += endTime;


        String response = "";

        response = this.comms.sendMessage(message);
        utils.println(response);
    }

    public void changeBooking(Utils utils, String bookingID, String facility){
        String message = SERVICENAME;
        message += "updateObject/" + facility + "/changeBooking/" + bookingID + "/";
        int offset = utils.checkUserIntInput(-24,24);
        message += offset;
        String response = "";

        response = this.comms.sendMessage(message);

        utils.println(response);
    }

    public void monitorFacility(String facility, String username, Utils utils) throws IOException {
        //monitor/register/facility/username/Port

        int duration = utils.UserInputOptions(1, 100,
                "Enter the duration (in seconds) to monitor from (1-100) inclusive",
                "Please reenter the duration (1-100) inclusive: ");

        String message = SERVICENAME + "monitor/register/" + facility + "/" + username + "/";   //register for notification
        String localPort = comms.getLocalPort();
        message += localPort;
        utils.println("monitoring....");
        this.comms.sendMessage(message);

        try {
            this.comms.monitorForNotification(duration, utils);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //monitor/unregister/facility/username
        message = SERVICENAME + "monitor/unregister/" + facility + "/" + username;    //remove from notification list
        this.comms.sendMessage(message);

        utils.println("Monitoring has ended\n");
    }

    public void getUserBookings(String facility, String username, Utils utils){
        //getBookings/facility/username
        String message = SERVICENAME + "getBookings/" + facility + "/" + username;
        String response = "";
        response = this.comms.sendMessage(message);
        utils.println(response);
    }

    public void extendBooking(String facility, String bookingID, int extensionTime, Utils utils){
        //extendBooking/facility/bookingID/extenstionTime
        String message = SERVICENAME + "extendBooking/" + facility + "/" + bookingID + "/" + extensionTime;
        String response = "";
        response = this.comms.sendMessage(message);
        utils.println(response);
    }

}
