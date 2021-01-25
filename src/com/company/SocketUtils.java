package com.company;

import java.io.*;
import java.net.Socket;

/**
 * This utils class provides input/output to and fro another socket connection
 */
public class SocketUtils implements Utils {

    InputStream input;
    InputStreamReader reader ;
    OutputStream output;
    PrintWriter writer;
    public SocketUtils(Socket socket) throws IOException {
        this.input  = socket.getInputStream();
        this.reader = new InputStreamReader(input);
        this.output = socket.getOutputStream();
        this.writer = new PrintWriter(output, true);
    }
    public String nextLine() {
        char character = 0;
        StringBuilder data = new StringBuilder();
        String s;
        do {
            while (true) {
                try {
                    character = (char) reader.read();
                    if (character == '\n' || character == '\r') break;
                    data.append(character);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            s = data.toString();
        } while (s.equals("") || s.equals("\r") || s.equals("\n"));
        return s;
    }
    
    public int nextInt() {
        String s;
        do {
            s = nextLine();
        } while ((s.equals("") || s.equals("\n") || s.equals("\r")));
        Integer a = Integer.valueOf(s);
        return a;
    }

    public int checkUserIntInput(int lower, int upper) {
        /**
         * This functions return an int value inclusive of upper and lower bound from user
         */
        int i;
        int tries = 1;
        String prompt;
        do {
            if (tries == 1)
                prompt = String.format("Please enter a number between %d and %d (inclusive): ",lower,upper);
            else
                prompt = String.format("Please reenter a valid number between %d and %d (inclusive): ",lower,upper);
            writer.println(prompt);
            writer.println(RRA.ACK);
            i = nextInt();
            tries++;
        } while (i>upper || i < lower);

        return i;
    }

    public int UserInputOptions(int start, int stop,String prompt,String reprompt){
        int i;
        int tries = 1;
        String p;
        do {
            if (tries ==1)
                p = prompt;
            else
                p = reprompt;
            writer.println(p);
            writer.println(RRA.ACK);
            i = nextInt();
            tries++;
        } while (i> stop || i < start);
        return i;
    }

    public String UserInputString(String prompt){
        String s;
        writer.println(prompt);
        writer.println(RRA.ACK);
        s = nextLine();
        return s;
    }

    public String getBookingID(){
        String prompt = "Please enter your booking ID String: ";
        writer.println(prompt);
        writer.println(RRA.ACK);
        String ans = nextLine();
        return ans;
    }

    public void println(String s) {
        writer.println(s);
    }
    public void print(String s){
        writer.print(s);
    }
}
