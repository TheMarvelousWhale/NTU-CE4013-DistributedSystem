package server;
import java.util.HashMap;

/**
 * The request handler class routes requests to different manager services (UserMgr, FacilityMgr ...)
 * where the manager services will handle the rest of the requests
 */
public class RequestHandler {
    public HashMap<String, ServiceMgr> registeredServices;

    public RequestHandler(){
        registeredServices = new HashMap<>();
    }

    /**
     * Registers new services
     */
    public void registerService(String serviceName, ServiceMgr newService){
        this.registeredServices.put(serviceName, newService);
    }

    public void handleRequest(String[] requestSequence){
        if (registeredServices.containsKey(requestSequence[0])){
            registeredServices.get(requestSequence[0]).handleRequest(requestSequence);
        }

    }
}
