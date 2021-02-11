package com.company;

import java.net.UnknownHostException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * The facility class that has availability array (7x24) and Record (track bookingID:Booking)
 * Anytime it needs to input/output something, need to specify the utils driver
 */
public class Facility {

    int[][] availability;
    String name;
    HashMap<String, Booking> Record;
    int ID;
    HashMap<String, String> registeredUsers;  // stores username and "ipAddress/portNumber" of registered User

    public Facility(String name,int ID) {
        /**
         * Facility class requires a name and an ID (just for safety checking)
         * Each instance keep track of its availability, which is a 7 x 24 array of int
         * 0 means available 1 means not available
         * It also keeps a record of which booking it has -- via the Record Hashmap
         */
        this.name = name;
        this.ID =ID;                                         //until now i Still dont know what this is for
        this.availability = new int[7][24];
        this.Record = new HashMap<>();
        this.registeredUsers = new HashMap<>();
    }


    private boolean checkAvailability(int date, int time) {
        /**
         * Check if a single time slot is available
         */
        if (this.availability[date][time] == 0)
            return true;
        else return false;
    }

    private void setAvailability(DayOfWeek date, int startTime, int endTime){
        for(int i= startTime; i<=endTime;i++) {
            this.availability[date.getValue()-1][i] = 1;
        }
    }

    private void clearAvailability(DayOfWeek date, int startTime, int endTime) {
        for(int i= startTime; i<=endTime;i++) {
            this.availability[date.getValue()-1][i] = 0;
        }
    }


    private boolean checkForClash(DayOfWeek date,int startTime, int endTime){
        for(int i= startTime; i<=endTime;i++) {
            if (this.availability[date.getValue()-1][i] == 1)
                return true;
        }
        return false;
    }

    public String queryAvailability() {
        /**
         * Print the availability of each facility
         */
        String timeTable = "";
        char[] days = {'M', 'T', 'W', 'T', 'F', 'S', 'S'};
        timeTable+="   |  0000|  0100|  0200|  0300|  0400|  0500|  0600|  0700|  0800|  0900|  1000| " +
                " 1100|  1200|  1300|  1400|  1500|  1600|  1700|  1800|  1900|  2000|  2100|  2200|  2300|\n";
        timeTable+="--------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------\n";
        for(int i = 0; i<7; i++){
           timeTable += days[i] + "  | ";
            for(int j = 0; j<24; j++){
                if (checkAvailability(i, j)) {
                    timeTable += "  O  | ";
                }
                else timeTable += "  X  | ";
            }
            timeTable += "\n";
        }

        timeTable += "\n";
        timeTable += "O = Available \nX = Booked \n\n";
        return  timeTable;
    }

    public String book(String username, String date_input, String startTime, String endTime, UDPServer sender){
        /**
         * Front end to print the facility to the user
         * Returns the points used for booking the facility
         */
        String returnString = "";
        DayOfWeek date = DayOfWeek.of(Integer.parseInt(date_input));

        if (this.checkForClash(date,Integer.parseInt(startTime),Integer.parseInt(endTime))) {
            returnString += "There is a clash with another booking";
        }
        else {
            this.setAvailability(date, Integer.parseInt(startTime), Integer.parseInt(endTime));
            String bookingID = UUID.randomUUID().toString();
            this.Record.put(bookingID, new Booking(bookingID, date, Integer.parseInt(startTime),
                    Integer.parseInt(endTime),username));

            returnString += queryAvailability();
            returnString += "Your booking is successful. Booking ID is "+bookingID;
            this.notifyUsers(sender);
        }

        return returnString;
    }



    public String changeBooking(String bookingID, String offsetStr, UDPServer sender) {
        /**
         * Change the booking given an ID
         */
        String returnString = "";
        int offset = Integer.parseInt(offsetStr);
        //verify there is such a booking
        if (!this.Record.containsKey(bookingID)) {
            returnString += "No such booking here.";
        }
        else {
            //get the details of the old Booking
            Booking b = this.Record.get(bookingID);
            DayOfWeek oldDate = b.date;
            int oldStartTime = b.startTime;
            int oldEndTime = b.endTime;

            //clear the old booking
            this.clearAvailability(oldDate,oldStartTime,oldEndTime);

            // in case of any error when we try to book the new booking, we need to rebook this thing
            //default to true -- so if new booking is feasible, set it to false
            boolean rebook = true;

            // can the booking be advanced/delayed ?
            returnString += "Current booking is from "+oldStartTime+" to "+oldEndTime + "\n";

            //check bound first, then check clash (because clash assume it is within bound of 7x24 array)
            if (oldStartTime+offset < 0 || oldEndTime+offset > 23)
                returnString += "Such change cannot be made as it exceeds the day boundary...Return to Main Page";
            else if (this.checkForClash(oldDate,oldStartTime+offset,oldEndTime+offset)) {
                returnString += "There is a clash with another booking";
            }
            else {
                //try to create a new booking
                DayOfWeek newDate = b.date;             //it is still the same date, but put here for clarity
                int newStartTime = oldStartTime+offset;
                int newEndTime = oldEndTime+offset;

                //change the availability array
                this.setAvailability(newDate,newStartTime,newEndTime);

                //change the Booking Record
                b.startTime = newStartTime;
                b.endTime = newEndTime;

                //booking is successful
                rebook = false;

                returnString += "Your booking on " + b.date + " has been changed to: " + b.startTime + "h - " + b.endTime + "h \n";
                this.notifyUsers(sender);
            }

            if (rebook) {
                this.setAvailability(oldDate, oldStartTime, oldEndTime);
                returnString += "Your booking " + bookingID + " was not modified\n";
            }
        }
        return returnString;
    }


    public void showRecords(String username,Utils utils) {
        /**
         * This show all the current booking ID of the user with their start and end time
         */
        utils.println("For user: "+ username);
        for (String i :this.Record.keySet()) {
            Booking b = this.Record.get(i);
            if (b.username.equals(username))
                utils.println("Booking ID:" + i + ", date: "+b.date.toString()+", from "+b.startTime+" to "+b.endTime);
            else
                utils.println("Another user has booked this facility on date: "+b.date.toString()+", from "+b.startTime+" to "+b.endTime);
        }
    }

    public int cancelBooking(String bookingID, Utils utils) {
        /**
         * Allows user to cancel booking given a valid booking ID
         */
        //verify there is such a booking
        if (!this.Record.containsKey(bookingID)) {
            utils.println("No such booking here.");
        } else {
            //get the details of the old Booking
            Booking b = this.Record.get(bookingID);
            DayOfWeek Date = b.date;
            int StartTime = b.startTime;
            int EndTime = b.endTime;

            //print details of old booking
            utils.println("Your booking on: " + b.date + ", from " + b.startTime + "h to " + b.endTime + "h, will be cancelled.");

            //clear on the Availability & Record, booking ID will become invalid.
            this.clearAvailability(Date, StartTime, EndTime);
            this.Record.remove(bookingID);
            return EndTime - StartTime + 1;
        }
        return 0;
    }

    public void extendBooking(String bookingID, Utils utils) {
        /**
         * Allows user to extend his current booking given a valid booking ID
         */
        // verify there is such a booking
        if (this.Record.containsKey(bookingID) == false){
            utils.println("No such booking here.");
        } else {
            //get the details of the old Booking
            Booking b = this.Record.get(bookingID);
            DayOfWeek Date = b.date;
            int StartTime = b.startTime;
            int EndTime = b.endTime;

            //clear the current booking to prevent clashing with itself
            this.clearAvailability(Date,StartTime,EndTime);

            //set to false if new booking successful, else rebook old booking
            boolean rebook = true;

            //Allow up to 3hrs of extension at once
            utils.println("How many slots do you want to extend your booking by (only up to 3hrs)?");
            int offset = utils.checkUserIntInput(1,3);

            //Ensures that it does not go to the next day
            if (EndTime+offset > 23) {
                utils.println("Such change cannot be made as it exceeds the day boundary...Return to Main Page");
            }
            //Ensures that it does not clash with existing bookings
            else if (this.checkForClash(Date,StartTime,EndTime+offset)) {
                utils.println("There is a clash with another booking");
            }
            //No clash, update the Availability, Record & lastModified. Show user booking is updated
            else {
                this.setAvailability(Date, StartTime, EndTime+offset);
                b.endTime = EndTime+offset;
                rebook = false;
                utils.println("Your booking on " + b.date + " has been extended. " + "Timeslot: " + b.startTime + "h - " + b.endTime + "h");
            }
            //If new booking fails, rebook the original timeslot. Show user booking is same.
            if (rebook){
                this.setAvailability(Date, StartTime, EndTime);
                utils.println("No changes made to your booking on " + b.date + ", Timeslot: " + b.startTime + "h - " + b.endTime + "h");
            }
        }
    }

    public void registerUser(String username, String address, String port){
        this.registeredUsers.put(username, (address + "/" + port));
    }

    public void unregisterUser(String username){
        this.registeredUsers.remove(username);
    }

    private void notifyUsers(UDPServer sender){
        Iterator hmIterator = this.registeredUsers.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            String value = (String) mapElement.getValue();
            String[] addressAndPort = value.split("/", -1);
            try {
                sender.sendNotifaction(addressAndPort[0], addressAndPort[1], "NOTIFICATION");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

}