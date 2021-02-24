package server;

public interface ServiceMgr {
    public boolean addObject(String key, Object value);
    public boolean checkObject(String key);
    public String updateObject(String key, String[] operation);
    public void handleRequest(String[] requestSequence);
}