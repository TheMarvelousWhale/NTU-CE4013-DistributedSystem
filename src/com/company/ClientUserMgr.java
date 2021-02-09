package com.company;

import java.io.IOException;

public class ClientUserMgr {
    private static ClientUserMgr single_instance = null;
    private static final String SERVICENAME = "UserService/";

    private ClientUserMgr(){

    }


    public static ClientUserMgr getInstance() {
        if (single_instance == null)
            single_instance = new ClientUserMgr();
        return single_instance;
    }

    public void Register(Utils utils, UDPClient sender){
        /**
         * Registers a new user into the UserRecords
         * Sends username to Server
         */
        String message = SERVICENAME + "checkObject/";
        String username = utils.UserInputString("Please enter a username: ");
        String response = "";
        try {
            response = sender.sendMessage(message + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!response.equals("success")) {
            try {
                response = sender.sendMessage(message + username);
                username = utils.UserInputString("Username already exists. Please enter a username: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        message = SERVICENAME + "addObject/";
        String password = utils.UserInputString("Please enter a password: ");
        try {
            response = sender.sendMessage(message + username + "/" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        utils.println("User account " + username + " has been successfully created.");
    }

    public String Login(Utils utils, UDPClient sender){
        /**
         * Logs in the user.
         * Returns the Username string for making requests under this username
         */
        String username = utils.UserInputString("LOGIN PAGE \n______________\nusername: ");

        String password = utils.UserInputString("password: ");
        String response = "";
        String message = SERVICENAME + "Login/";
        int attempts = 3;
        try {
            response = sender.sendMessage(message + username+ "/" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!response.equals("success")){
            utils.println("The password entered is incorrect. \nAttempts left: " + attempts + "\npassword: ");
            password = utils.nextLine();
            attempts-=1;
            try {
                response = sender.sendMessage(message + username+ "/" + password);
            } catch (IOException e) {
                e.printStackTrace();
            }
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