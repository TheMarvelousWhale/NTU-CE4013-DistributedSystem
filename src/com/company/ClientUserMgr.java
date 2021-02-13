package com.company;

import java.io.IOException;

public class ClientUserMgr {
    private static ClientUserMgr single_instance = null;
    private static final String SERVICENAME = "UserService/";
    UDPClient sender;

    private ClientUserMgr(UDPClient sender){
        this.sender = sender;
    }


    public static ClientUserMgr getInstance(UDPClient sender) {
        if (single_instance == null)
            single_instance = new ClientUserMgr(sender);
        return single_instance;
    }

    public void Register(Utils utils){
        /**
         * Registers a new user into the UserRecords
         * Sends username to Server
         */
        String message = SERVICENAME + "checkObject/";
        String username = utils.UserInputString("Please enter a username: ");
        String response = "";
        response = sender.sendMessage(message + username);
        while (!response.equals("success")) {
            username = utils.UserInputString("Username already exists. Please enter a username: ");
            response = sender.sendMessage(message + username);
        }

        message = SERVICENAME + "addObject/";
        String password = utils.UserInputString("Please enter a password: ");
        response = sender.sendMessage(message + username + "/" + password);
        utils.println("User account " + username + " has been successfully created.");
    }

    public String Login(Utils utils){
        /**
         * Logs in the user.
         * Returns the Username string for making requests under this username
         */
        String username = utils.UserInputString("LOGIN PAGE \n______________\nusername: ");

        String password = utils.UserInputString("password: ");
        String response = "";
        String message = SERVICENAME + "Login/";
        int attempts = 3;
        response = sender.sendMessage(message + username+ "/" + password);
        while (!response.equals("success") & attempts>0){
            utils.println("The password entered is incorrect. \nAttempts left: " + attempts + "\npassword: ");
            password = utils.nextLine();
            attempts-=1;
            response = sender.sendMessage(message + username+ "/" + password);
        }

        if (attempts > 0){
            utils.println("Login Successful");
            return username;
        }
        else{
            utils.println("Login failed.");
            return null;
        }
    }
}
