package server;

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
        /**
         * Don't know how is it different from the above function but okay.....
         */
        int i;
        int tries = 1;
        String p;
        do {
            if (tries ==1)
                p = prompt;
            else
                p = reprompt;
            tries++;
            System.out.println(p);
            i = s.nextInt();
            s.nextLine();
        } while (i> stop || i < start);
        return i;
    }

    public String UserInputString(String prompt) {
        System.out.println(prompt);
        String str = TerminalUtils.s.nextLine();
        return str;
    }

    public String getBookingID(){
        String prompt = "Please enter your booking ID String: ";
        System.out.println(prompt);
        return s.nextLine();
    }

    public void println(String s) {
        System.out.println(s);
    }

    public void print(String s) {
        System.out.print(s);
    }
}
