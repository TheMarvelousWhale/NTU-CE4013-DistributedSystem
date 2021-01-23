package com.company;

import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.util.HashMap;

public class Facility {

    int[][] availability;
    String name;
    HashMap<String, Booking> Record;
    int ID;
    long lastModified;

    public Facility(String name,int ID) {
        /**
         * Facility class requires a name and an ID (just for safety checking)
         * Each instance keep track of its availability, which is a 7 x 24 array of int
         * 0 means available not ID means not available
         */

        this.name = name;
        this.ID =ID;
        this.availability = new int[7][24];
        this.Record = new HashMap<String, Booking>();
        this.lastModified = System.currentTimeMillis();
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

    public long getLastModified() {
        return this.lastModified;
    }


    public void queryAvailability(Utils utils) {
        /**
         * Print the availability of each facility
         */
        char[] days = {'M', 'T', 'W', 'T', 'F', 'S', 'S'};
        utils.println("   |  0100|  0200|  0300|  0400|  0500|  0600|  0700|  0800|  0900|  1000| " +
                " 1100|  1200|  1300|  1400|  1500|  1600|  1700|  1800|  1900|  2000|  2100|  2200|  2300|  2400|");
        utils.println("--------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------");
        for(int i = 0; i<7; i++){
            utils.print(days[i] + "  | ");
            for(int j = 0; j<24; j++){
                if (checkAvailability(i, j)) {
                    utils.print("  O  | ");
                }
                else utils.print("  X  | ");
            }
            utils.print("\n");
        }

        utils.print("\n");
        utils.println("O = Available \nX = Booked \n");

    }

    public void book(String UUID, Utils utils){
        /**
         * Front end to print the facility to the user
         */

        utils.println("For the day (1: Monday, 2: Tuesday, 3: Wednesday, 4: Thursday, 5: Friday, 6: Saturday, 7: Sunday): ");
        int date_input = utils.checkUserIntInput(1,7);
        DayOfWeek date = DayOfWeek.of(date_input);
        utils.println("For the start time: ");
        int startTime = utils.checkUserIntInput(0,23);
        utils.println("For the end time: ");
        int endTime = utils.checkUserIntInput(0,23);
        this.setAvailability(date,startTime, endTime);
        this.Record.put(UUID,new Booking(UUID,date,startTime,endTime));
        queryAvailability(utils);
        //set last Modified
        this.lastModified = System.currentTimeMillis();
    }



    public void changeBooking(String bookingID,Utils utils) {
        /**
         * Change the booking given an ID
         */

        //verify there is such a booking
        if (this.Record.containsKey(bookingID) == false) {
            utils.println("No such booking here.");
        }
        else {
            //If there is, clear the old booking
            Booking b = this.Record.get(bookingID);
            int oldStartTime = b.startTime;
            int oldEndTime = b.endTime;
            this.clearAvailability(b.date,oldStartTime,oldEndTime);
            boolean rebook = true; // in case of any error, we need to rebook this thing

            // can the booking be advanced/delayed ?
            utils.println("Current booking is from "+oldStartTime+" to "+oldEndTime);
            utils.println("Please input the offset for booking ID: "+bookingID);
            int offset = utils.checkUserIntInput(-24,24);

            //check bound
            if (oldStartTime+offset < 0 || oldEndTime+offset > 23)
                utils.println("Such change cannot be made as it exceeds the day boundary...Return to Main Page");
            else if (this.checkForClash(b.date,oldStartTime+offset,oldStartTime+offset)) {
                utils.println("There is a clash with another booking");
            }
            else {
                //try to create a booking a
                int newStartTime = oldStartTime+offset;
                int newEndTime = oldEndTime+offset;

                this.setAvailability(b.date,newStartTime,newEndTime);

                //change the Booking Record
                b.startTime = newStartTime;
                b.endTime = newEndTime;
                //change Record
                rebook = false;
                //set last Modified
                this.lastModified = System.currentTimeMillis();
            }

            if (rebook)
                this.clearAvailability(b.date,oldStartTime,oldEndTime);
        }
    }
    private boolean checkForClash(DayOfWeek date,int startTime, int endTime){
        for(int i= startTime; i<=endTime;i++) {
            if (this.availability[date.getValue()-1][i] == 1)
                return true;
        }
        return false;
    }

    public void showRecords(Utils utils) {
        /**
         * This show all the current booking ID with their start and end time
         * TODO: should we add an user field to this booking ID? IDK man
         */

        for (String i :this.Record.keySet()) {
            Booking b = this.Record.get(i);
            utils.println("Booking ID:" + i + ", date: "+b.date.toString()+", from "+b.startTime+" to "+b.endTime);
        }
    }



    public void monitorSlot(int duration) {
        //TODO: HOW?


    }
}
