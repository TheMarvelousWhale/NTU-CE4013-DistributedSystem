package com.company;

import java.io.IOException;
import java.net.*;


public class UDPClient {
    String hostname = "";
    int port;
    DatagramSocket clientSocket;
    InetAddress IPAdress;

    public UDPClient(String hostname, int port) throws SocketException, UnknownHostException {
        this.hostname = hostname;
        this.port = port;
        this.clientSocket = new DatagramSocket(); //create an empty UDP socket
        this.IPAdress = InetAddress.getByName(hostname);
    }

    public String sendMessage(String message) throws IOException {
        // Send the message
        byte[] tx_buf;
        byte[] rx_buf = new byte[1024];
        tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf,tx_buf.length,IPAdress,this.port);
        this.clientSocket.send(txPacket);
        //System.out.println("Sent " + message + " to server");
        // Wait for ACK
        DatagramPacket rxPacket = new DatagramPacket(rx_buf,rx_buf.length);
        clientSocket.receive(rxPacket);
        String ack = new String(rxPacket.getData(),0,rxPacket.getLength());
        //clientSocket.close();
        return ack;
    }

}