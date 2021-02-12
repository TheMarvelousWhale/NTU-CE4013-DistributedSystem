package com.company;

import java.io.IOException;
import java.net.*;


public class UDPClient {
    String hostname = "";
    int serverPort;
    DatagramSocket clientSocket;
    InetAddress IPAdress;
    InetAddress localAddress;
    int localPort;

    public UDPClient(String hostname, int serverPort) throws SocketException, UnknownHostException {
        this.hostname = hostname;
        this.serverPort = serverPort;
        this.clientSocket = new DatagramSocket(); //create an empty UDP socket
        this.IPAdress = InetAddress.getByName(hostname);
        this.localPort = clientSocket.getLocalPort();
        this.localAddress = InetAddress.getLocalHost();
    }

    public String sendMessage(String message) throws IOException {
        // Send the message
//        clientSocket.setSoTimeout(2000);
        byte[] tx_buf;
        byte[] rx_buf = new byte[2048];
        tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf,tx_buf.length,IPAdress,this.serverPort);
        this.clientSocket.send(txPacket);
        //System.out.println("Sent " + message + " to server");
        // Wait for ACK
        DatagramPacket rxPacket = new DatagramPacket(rx_buf,rx_buf.length);
        try {
            clientSocket.receive(rxPacket);
        }
        catch (SocketTimeoutException e){
            System.out.println("Server not responding");
            return null;
        }

        String ack = new String(rxPacket.getData(),0,rxPacket.getLength());
        //clientSocket.close();
        return ack;
    }

    public void monitorForNotification(int monitorDuration, Utils utils) throws IOException {
        monitorDuration *= 1000;
        this.clientSocket.setSoTimeout(monitorDuration);
        byte[] rx_buf = new byte[2048];
        long prevTime = System.currentTimeMillis();
        long currTime;

        while(true) {
            DatagramPacket notification = new DatagramPacket(rx_buf, rx_buf.length);
            try {
                clientSocket.receive(notification);
                utils.println(new String(notification.getData(), 0, rx_buf.length));
            } catch (SocketTimeoutException e) {
                break;
            }
            currTime = System.currentTimeMillis();
            this.clientSocket.setSoTimeout((int)(monitorDuration - (currTime - prevTime)));
            prevTime = currTime;
        }
    }

    public String getLocalPort(){
        return String.valueOf(localPort);
    }

    public String getLocalAddress(){
        String localAddress = this.localAddress.toString();
        String [] addresses = localAddress.split("/", -1);
        System.out.println(addresses[0]);
        return addresses[0];
    }

}