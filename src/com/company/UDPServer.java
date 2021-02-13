package com.company;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;


public class UDPServer {

    public static final int PORT = 9000;
    public DatagramSocket serverSocket;
    public InetAddress returnAddress;
    public int returnPort;
    public HashMap<String , String []> history;
    public String requestID;

    public UDPServer() throws SocketException {
        this.serverSocket = new DatagramSocket(PORT);
        this.history = new HashMap<>();
    }

    public InetAddress getReturnAddress(){
        return returnAddress;
    }

    public String[] receiveRequests(){
        /**
         * Receives requests from clients
         * And updates the returnAddress to the previously received packet
         */
        byte[] rx_buf = new byte[2048];
        DatagramPacket rxPacket = new DatagramPacket(rx_buf, rx_buf.length);
        try {
            serverSocket.receive(rxPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String requestString = new String(rxPacket.getData(), 0, rxPacket.getLength());
        System.out.println(requestString);
        String[] requestSequence = requestString.split("/", -1);

        this.returnAddress = rxPacket.getAddress();
        this.returnPort = rxPacket.getPort();

        String key = this.returnAddress.toString() + "/" +  this.returnPort;
        String requestID = requestSequence[requestSequence.length - 1];
        if (this.history.containsKey(key)){
            // we sent to this client before
            if (this.history.get(key)[1].equals(requestID)){
                //  the package was sent before, resend the same message
                this.resendPacket(this.history.get(key)[0], this.history.get(key)[1]);
                return null;
            }
        }
        else{
            this.history.put(key, new String[]{"", ""});
            this.requestID = requestID;
        }

        return requestSequence;
    }


    public void sendMessage(String message){
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.updateHistory(message, this.requestID);   // update the history
    }


    public void sendSuccessMessage(){
        String message = "success";
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendFailureMessage(){
        String message = "fail";
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateHistory(String replyMessage, String requestID){
        this.history.replace(this.returnAddress.toString() + "/" + this.returnPort, new String[]{replyMessage, requestID});
    }

    private void resendPacket(String addressAndPort, String message){
        String[] addressAndPortArr = addressAndPort.split("/", 1);
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = null;
        try {
            txPacket = new DatagramPacket(tx_buf, tx_buf.length, InetAddress.getByName(addressAndPortArr[0])
                    , Integer.parseInt(addressAndPortArr[1]));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendNotifaction(String address, String port, String message) throws UnknownHostException {
        byte[] tx_buf = message.getBytes();
        System.out.println(address + " " + port);
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, InetAddress.getByName(address), Integer.parseInt(port));
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}