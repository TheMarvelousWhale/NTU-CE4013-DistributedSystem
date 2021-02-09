package com.company;

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
     * @param serviceName
     * @param newService
     */
    public void registerService(String serviceName, ServiceMgr newService){
        this.registeredServices.put(serviceName, newService);
    }

    public void handleRequest(String requestString, UDPServer sender){
        String[] requestSequence = requestString.split("/", -1);
        registeredServices.get(requestSequence[0]).handleRequest(requestSequence, sender);
    }
}
