package com.company;

import java.util.HashMap;

/**
 * This class just needs to check if username exists
 * And create user objects when requested
 */

public class ServerUserMgr {
    private static ServerUserMgr single_instance = null;
    public HashMap<String, User> UserRecords;

    private ServerUserMgr(){
        this.UserRecords = new HashMap<String, User>();
    }

    public static ServerUserMgr getInstance() {
        if (single_instance == null)
            single_instance = new ServerUserMgr();
        return single_instance;
    }

    public void checkUsername(String username, UDPServer sender){
        if (!this.UserRecords.containsKey(username)){
            sender.sendMessage("success");
        }
        else {
            sender.sendMessage("fail");
        }
    }

    public void addNewuser(String username, String password, UDPServer sender){
        User newUser = new User(username, password);
        this.UserRecords.put(username, newUser);
        sender.sendMessage("success");
    }

    public void login(String username, String password, UDPServer sender){
        if (this.UserRecords.containsKey(username)){
            if (this.UserRecords.get(username).password.equals(password)){
                sender.sendMessage("success");
            }
            else{
                sender.sendMessage("fail");
            }
        }
        else{
            sender.sendMessage("fail");
        }
    }
}
