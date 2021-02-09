package com.company;

public interface ServiceMgr {
    public boolean addObject(String key, Object value);
    public boolean checkObject(String key);
    public boolean updateObject(String key, String[] operation);
    public void handleRequest(String[] requestSequence, UDPServer sender);
}