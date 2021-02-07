package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


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
        byte[] rx_buf = new byte[1024];
        DatagramPacket rxPacket = new DatagramPacket(rx_buf, rx_buf.length);
        try {
            serverSocket.receive(rxPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sentence = new String(rxPacket.getData(), 0, rxPacket.getLength());
        //System.out.println("Received "+sentence);
        this.returnAddress = rxPacket.getAddress();
        this.returnPort = rxPacket.getPort();
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

}