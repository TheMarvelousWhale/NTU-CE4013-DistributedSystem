package server;

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


}
