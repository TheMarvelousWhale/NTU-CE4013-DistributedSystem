package com.company;

import java.io.IOException;
import java.net.*;


public class UDPServer {

    public static final int PORT = 9000;
    public DatagramSocket serverSocket;
    public InetAddress returnAddress;
    public int returnPort;

    public UDPServer() throws SocketException {
        this.serverSocket = new DatagramSocket(PORT);
    }

    public String receiveRequests(){
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
        String sentence = new String(rxPacket.getData(), 0, rxPacket.getLength());

        this.returnAddress = rxPacket.getAddress();
        this.returnPort = rxPacket.getPort();
//        System.out.println("Received "+sentence + " from " + this.returnAddress + ":" + this.returnPort);
        return sentence;
    }

    public void sendMessage(String message){
        byte[] tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf, tx_buf.length, this.returnAddress, this.returnPort);
        try {
            serverSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //serverSocket.close();
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