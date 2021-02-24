package server;
import java.util.HashMap;

/**
 * This class just needs to check if username exists
 * And create user objects when requested
 */

public class ServerUserMgr implements ServiceMgr {
    private static ServerUserMgr single_instance = null;
    public HashMap<String, User> UserRecords;
    UDPServer sender;

    private ServerUserMgr(UDPServer sender){
        this.UserRecords = new HashMap<>();
        this.sender = sender;
    }

    public static ServerUserMgr getInstance(UDPServer sender) {
        if (single_instance == null)
            single_instance = new ServerUserMgr(sender);
        return single_instance;
    }

    public void checkUsername(String username){
        if (!this.UserRecords.containsKey(username)){
            sender.sendMessage("success");
        }
        else {
            sender.sendMessage("fail");
        }
    }

    public void addNewuser(String username, String password){
        User newUser = new User(username, password);
        this.UserRecords.put(username, newUser);
        sender.sendMessage("success");
    }

    public boolean login(String username, String password){
        if (this.UserRecords.containsKey(username)){
            if (this.UserRecords.get(username).password.equals(password)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public boolean addObject(String username, Object newUser) {
        if (this.UserRecords.containsKey(username))
            return false;
        this.UserRecords.put(username, (User) newUser);
        return true;
    }

    @Override
    public boolean checkObject(String username) {
        return !this.UserRecords.containsKey(username);
    }

    @Override
    public String updateObject(String key, String[] operation) {
        if (!this.UserRecords.containsKey(key))
            return "failure";

        if (operation[3].equals("username")){
            UserRecords.get(key).username = operation[4];
        }
        else{
            UserRecords.get(key).password = operation[4];
        }
        return "success";
    }

    @Override
    public void handleRequest(String[] requestSequence) {
        switch (requestSequence[1]){
            case "addObject":  //  addObject/username/password
                this.addObject(requestSequence[2], new User(requestSequence[2], requestSequence[3]));
                sender.sendSuccessMessage();
                break;

            case "Login":  // Login/username/password
                if (this.login(requestSequence[2], requestSequence[3]))
                    sender.sendSuccessMessage();
                else
                    sender.sendFailureMessage();
                break;

            case "checkObject":   // checkObject/username
                if (this.checkObject(requestSequence[2]))
                    sender.sendSuccessMessage();
                else
                    sender.sendFailureMessage();
                break;

            case "updateObject":    //   updateObject/$username/$(username or password)/$newValue
                sender.sendMessage(this.updateObject(requestSequence[2], requestSequence));
                break;

            default:
                sender.sendFailureMessage();
                break;
        }
    }
}
