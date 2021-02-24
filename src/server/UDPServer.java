package server;

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
    public boolean atMostOnceInvocation = true;

    public boolean serverSidePacketLoss = false;

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

        if (requestSequence[0].equals("atLeastOnceInvocation")) {
            this.atMostOnceInvocation = false;
            this.sendMessage("success");
        }

        String key = this.returnAddress.toString() + "/" +  this.returnPort;

        String requestID = requestSequence[requestSequence.length - 1];
        this.requestID = requestID;

        if (this.history.containsKey(key)){
            // we sent to this client before
            System.out.println("received from the same client");
            System.out.println("previous request ID: " + this.history.get(key)[1]);
            System.out.println("current request ID: " + requestID);
            if (!atMostOnceInvocation){
                this.history.remove(key);
            }

            if (atMostOnceInvocation && this.history.get(key)[1].equals(requestID)){
                //  the package was sent before, resend the same message
                System.out.println("resending message: \n" + this.history.get(key)[0]);
                this.resendPacket(key, this.history.get(key)[0]);
                return null;
            }
        }
        else
            this.history.put(key, new String[]{"", ""});


        if (requestSequence[0].equals("set server packet loss"))    // set the server to lose packet
            this.setPacketLoss(requestID);

        return requestSequence;
    }


    public void sendMessage(String message){
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            if (!serverSidePacketLoss) {
                serverSocket.send(txPacket);
            }
            else{ // if stimulating serverSide Packet loss, dont send packet
                if (!history.containsKey(this.returnAddress.toString() + "/" +  this.returnPort))
                    serverSocket.send(txPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.updateHistory(message, this.requestID);   // update the history
    }


    public void sendSuccessMessage(){
        String message = "success";
        this.sendMessage(message);
    }


    public void sendFailureMessage(){
        String message = "fail";
        this.sendMessage(message);
    }


    public void updateHistory(String replyMessage, String requestID){
        if (atMostOnceInvocation)
            this.history.replace(this.returnAddress.toString() + "/" + this.returnPort,
                    new String[]{replyMessage, requestID});
    }

    private void resendPacket(String addressAndPort, String message){
        String[] addressAndPortArr = addressAndPort.split("/", -1);
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = null;
        System.out.println(addressAndPortArr[1] + " : " + addressAndPortArr[2]);
        try {
            txPacket = new DatagramPacket(tx_buf, tx_buf.length, InetAddress.getByName(addressAndPortArr[1])
                    , Integer.parseInt(addressAndPortArr[2]));
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

    public void setPacketLoss(String requestID){
        this.serverSidePacketLoss = true;
        String message = "success";
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.updateHistory(message, requestID);
    }


}