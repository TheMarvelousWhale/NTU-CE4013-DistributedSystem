package com.company;

import java.util.HashMap;

public class UserMgr {
    private static UserMgr single_instance = null;
    public HashMap<String, User> UserRecords;

    private UserMgr(){
        this.UserRecords = new HashMap<String, User>();
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
        utils.println("Please enter a username: ");
        utils.nextLine();
        String username = utils.nextLine();

        while (UserRecords.containsKey(username)){
            utils.println("The username entered already exists. \nPlease enter another username.");
            username = utils.nextLine();
        }

        utils.println("Please enter a password: ");
        String password = utils.nextLine();
        User newUser = new User(username, password);
        UserRecords.put(username, newUser);

        utils.println("User account " + username + " has been successfully created.");
    }

    public User Login(Utils utils){
        /**
         * Logs in the user.
         * Returns the User object for the other classes to use
         */
        utils.println("LOGIN PAGE \n______________\nusername: ");
        utils.nextLine();
        String username = utils.nextLine();

        if (!UserRecords.containsKey(username)){
            utils.println("The username entered does not exists.");
            return null;
        }
        utils.println("password: ");
        String password = utils.nextLine();
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
