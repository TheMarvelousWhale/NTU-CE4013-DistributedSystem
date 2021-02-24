package client;

import java.io.IOException;
import java.net.*;


public class UDPClient {
    String hostname = "";
    int serverPort;
    DatagramSocket clientSocket;
    InetAddress IPAdress;
    InetAddress localAddress;
    int localPort;
    String mode = "normal";
    int requestID = 0;

    public UDPClient(String hostname, int serverPort) throws SocketException, UnknownHostException {
        this.hostname = hostname;
        this.serverPort = serverPort;
        this.clientSocket = new DatagramSocket(); //create an empty UDP socket
        this.IPAdress = InetAddress.getByName(hostname);
        this.localPort = clientSocket.getLocalPort();
        this.localAddress = InetAddress.getLocalHost();
    }

    public String sendMessage(String message) {
        int targetPort = this.serverPort;
        message += ("/" + this.requestID);   // append request ID to the end of the message
        this.requestID += 1;   // increase request ID

        if (this.mode.equals("client signal loss")){
            targetPort = 1222; // purposely send to wrong port to stimulate signal loss
        }

        try {
            clientSocket.setSoTimeout(2000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] rx_buf = new byte[2048];

        sendPacket(targetPort, this.IPAdress, message);     // send message


        DatagramPacket rxPacket = new DatagramPacket(rx_buf,rx_buf.length);

        try {
            clientSocket.receive(rxPacket);
        }
        catch (SocketTimeoutException e){
//            System.out.println("Server not responding, packet loss detected. \nResending request.");
            e.printStackTrace();
            targetPort = this.serverPort;   // reset the port to the correct port number
            sendPacket(targetPort, this.IPAdress, message);   // resend packet
            try {
                clientSocket.receive(rxPacket);   // try to receive again
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ack = new String(rxPacket.getData(),0,rxPacket.getLength());
//        System.out.println(ack);
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

    private void sendPacket(int port, InetAddress address, String message){
        byte[] tx_buf;
        tx_buf = message.getBytes();
        DatagramPacket txPacket = new DatagramPacket(tx_buf,tx_buf.length,address,port);
        try {
            this.clientSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMode(String mode){
        this.mode = mode;
        if (this.mode.equals("server signal loss")){
            this.sendMessage("set server packet loss");
            System.out.println("Server side packet loss has been set.");
        }
    }

}