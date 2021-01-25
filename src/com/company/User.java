package com.company;

public class User {

    String username;
    String password;
    int    bookingPoints;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.bookingPoints = 10;
    }

    public void queryBookingPoints(Utils utils){
        /**
         * prints available booking points of this user
         */
        utils.println("User: " + this.username + "\nBooking Points: " + this.bookingPoints);
    }

    public void bookFacility(Utils utils, Facility facility){
        /**
         * User will be able to book facilities here, bookint points are passed to the facility
         */
        this.bookingPoints -= facility.book(this.username, this.bookingPoints, utils);
    }

    public void queryFacilityAvailability(Utils utils, Facility facility){
        /**
         * Users can query the availability of a Facility
         */
        facility.queryAvailability(utils);
    }

    public void getBookingRecords(Utils utils, Facility facility){
        /**
         * Users can get their booking records
         */
        facility.showRecords(this.username, utils);
    }

    public void cancelBooking(Utils utils, Facility facility){
        this.getBookingRecords(utils, facility);
        String cancelID = utils.getBookingID();
        int pointsRedeemed = facility.cancelBooking(cancelID, utils);
        this.bookingPoints += pointsRedeemed;
        utils.println("Points redeemed: " + pointsRedeemed +"\nPoints Available: " + this.bookingPoints);

    }



}
