package com.company;

import java.util.Scanner;

/**
 * This class provides input/output to the terminal -- good for testing server functionality
 */
public class TerminalUtils implements Utils {
    public static Scanner s = new Scanner(System.in);

    public String nextLine(){
        String sc = s.nextLine();
        return sc;
    }
    public int checkUserIntInput(int lower, int upper ) {
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
            System.out.println(prompt);
            i = s.nextInt();
            tries++;
        } while (i>upper || i < lower);
        return i;
    }

    public int UserInputOptions(int start, int stop,String prompt,String reprompt) {
        int i;
        int tries = 1;
        String p;
        do {
            if (tries ==1)
                p = prompt;
            else
                p = reprompt;
            System.out.println(p);
            i = s.nextInt();
        } while (i> stop || i < start);
        return i;
    }

    public String UserInputString(String prompt) {
        System.out.println(prompt);
        String s = nextLine();
        return s;
    }

    public String getBookingID(){
        String prompt = "Please enter your booking ID String: ";
        System.out.println(prompt);
        s.nextLine();                      //clear stuff
        String ans = s.nextLine();         //
        return ans;
    }

    public void println(String s) {
        System.out.println(s);
    }

    public void print(String s) {
        System.out.print(s);
    }
}
