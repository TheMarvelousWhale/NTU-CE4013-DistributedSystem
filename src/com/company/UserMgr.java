package com.company;

import java.util.HashMap;

public class UserMgr {
    private static UserMgr single_instance = null;
    public HashMap<String, User> UserRecords;

    private UserMgr(){
        this.UserRecords = new HashMap<String, User>();

        //default account
        this.UserRecords.put("admin",new User("admin","admin"));
        this.UserRecords.get("admin").bookingPoints=1000;
        }


    public static UserMgr getInstance() {
        if (single_instance == null)
            single_instance = new UserMgr();
        return single_instance;
    }

    public void Register(Utils utils){
        /**
         * Registers a new user into the UserRecords
         */

        String username = utils.UserInputString("Please enter a username: ");

        while (UserRecords.containsKey(username)){
            utils.println("The username entered already exists.");
            username = utils.UserInputString("Please enter another username: ");
        }

        String password = utils.UserInputString("Please enter a password: ");
        User newUser = new User(username, password);
        UserRecords.put(username, newUser);

        utils.println("User account " + username + " has been successfully created.");
    }

    public User Login(Utils utils){
        /**
         * Logs in the user.
         * Returns the User object for the other classes to use
         */
        String username = utils.UserInputString("LOGIN PAGE \n______________\nusername: ");

        if (!UserRecords.containsKey(username)){
            utils.println("The username entered does not exists.");
            return null;
        }
        String password = utils.UserInputString("password: ");
        int attempts = 3;
        while (!password.equals(UserRecords.get(username).password) && attempts > 0){
            utils.println("The password entered is incorrect. \nAttempts left: " + attempts + "\npassword: ");
            password = utils.nextLine();
            attempts-=1;
        }

        if (attempts > 0){
            utils.println("Login Successful");
            return UserRecords.get(username);
        }
        else{
            utils.println("Login failed.");
            return null;
        }
    }

}
